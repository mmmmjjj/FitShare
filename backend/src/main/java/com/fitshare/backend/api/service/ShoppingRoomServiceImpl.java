package com.fitshare.backend.api.service;

import com.fitshare.backend.api.request.MakeShoppingRoomReq;
import com.fitshare.backend.api.response.ShoppingRoomRes;
import com.fitshare.backend.api.response.ShoppingRoomTokenRes;
import com.fitshare.backend.common.exception.ExceedParticipantCountException;
import com.fitshare.backend.common.exception.MemberNotFoundException;
import com.fitshare.backend.common.exception.ShoppingMallNotFoundException;
import com.fitshare.backend.common.exception.ShoppingRoomNotFoundException;
import com.fitshare.backend.db.entity.Member;
import com.fitshare.backend.db.entity.RoomParticipant;
import com.fitshare.backend.db.entity.ShoppingMall;
import com.fitshare.backend.db.entity.ShoppingRoom;
import com.fitshare.backend.db.repository.MemberRepository;
import com.fitshare.backend.db.repository.RoomParticipantRepository;
import com.fitshare.backend.db.repository.ShoppingMallRepository;
import com.fitshare.backend.db.repository.ShoppingRoomRepository;
import io.openvidu.java.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ShoppingRoomServiceImpl implements ShoppingRoomService {

    private MemberRepository memberRepository;
    private ShoppingRoomRepository shoppingRoomRepository;
    private ShoppingMallRepository shoppingMallRepository;
    private RoomParticipantRepository roomParticipantRepository;

    // openvidu 객체
    private OpenVidu openVidu;

    // openvidu server 관련 변수
    private String OPENVIDU_URL;
    private String SECRET;

    // openvidu 세션 정보 저장
    private Map<Long, Session> mapSessions = new ConcurrentHashMap<>();

    // openvidu 세션 참여자 정보 저장
    private Map<Long, Map<Long, String>> mapSessionNamesTokens = new ConcurrentHashMap<>();

    public ShoppingRoomServiceImpl(MemberRepository memberRepository, ShoppingRoomRepository shoppingRoomRepository, ShoppingMallRepository shoppingMallRepository,
                                   RoomParticipantRepository roomParticipantRepository,
                                   @Value("${openvidu.url}") String openviduUrl, @Value("${openvidu.secret}") String secret) {
        this.memberRepository = memberRepository;
        this.shoppingRoomRepository = shoppingRoomRepository;
        this.shoppingMallRepository = shoppingMallRepository;
        this.roomParticipantRepository = roomParticipantRepository;

        this.OPENVIDU_URL = openviduUrl;
        this.SECRET = secret;
        this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
    }

    /**
     * 쇼핑룸 생성
     **/
    @Transactional
    @Override
    public ShoppingRoomTokenRes createShoppingRoom(Long memberId, MakeShoppingRoomReq req) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

        ShoppingRoom shoppingRoom = ShoppingRoom.builder()
                .host(member)
                .shoppingMallName(req.getShoppingMallName())
                .shoppingMallUrl(req.getShoppingMallUrl())
                .participantCount(req.getParticipantCount())
                .password(req.getPassword())
                .isPrivate(req.isPrivate())
                .isCustomShoppingMall(req.isCustomShoppingMall())
                .isActive(true)
                .build();

        // DB에 있는 쇼핑몰을 사용할 경우
        if (!req.isCustomShoppingMall()) {
            ShoppingMall shoppingMall = shoppingMallRepository.findById(req.getShoppingMallId())
                    .orElseThrow(() -> new ShoppingMallNotFoundException(req.getShoppingMallId()));
            shoppingRoom.setShoppingMall(shoppingMall);

            shoppingRoom.setShoppingMallName(shoppingMall.getName());
            shoppingRoom.setShoppingMallUrl(shoppingMall.getUrl());
        }

        // 쇼핑룸 정보 DB 저장
        shoppingRoomRepository.save(shoppingRoom);
        // host member 참여자 DB에 저장
        roomParticipantRepository.save(new RoomParticipant(shoppingRoom, member));

        String sessionToken = getSessionToken(OpenViduRole.PUBLISHER, shoppingRoom.getId(), member);

        return new ShoppingRoomTokenRes(shoppingRoom.getId(), shoppingRoom.getShoppingMallName(), shoppingRoom.getShoppingMallUrl(), sessionToken);
    }

    /**
     * 쇼핑룸 입장
     **/
    @Transactional
    @Override
    public ShoppingRoomTokenRes enterShoppingRoom(Long memberId, Long shoppingRoomId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        ShoppingRoom shoppingRoom = shoppingRoomRepository.findById(shoppingRoomId).orElseThrow(() -> new ShoppingRoomNotFoundException(shoppingRoomId));

        // 입장 가능 인원 확인
        if (shoppingRoom.getParticipantCount() <= mapSessionNamesTokens.get(shoppingRoomId).size()) {
            throw new ExceedParticipantCountException();
        }

        String sessionToken = getSessionToken(OpenViduRole.PUBLISHER, shoppingRoomId, member);

        // 참여자 DB 저장
        roomParticipantRepository.save(new RoomParticipant(shoppingRoom, member));

        return new ShoppingRoomTokenRes(shoppingRoomId, shoppingRoom.getShoppingMallName(), shoppingRoom.getShoppingMallUrl(), sessionToken);
    }

    /**
     * 쇼핑룸 나가기
     **/
    @Override
    public void exitShoppingRoom(Long memberId, Long shoppingRoomId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

        // 나가려는 세션이 없다면
        if (this.mapSessions.get(shoppingRoomId) == null || this.mapSessionNamesTokens.get(shoppingRoomId) == null)
            throw new ShoppingRoomNotFoundException(shoppingRoomId);

        if (this.mapSessionNamesTokens.get(shoppingRoomId).remove(member.getId()) != null) {
            // 모두 방을 나갔다면
            if (this.mapSessionNamesTokens.get(shoppingRoomId).isEmpty()) {
                // 세션 삭제
                closeSession(shoppingRoomId);
                // 쇼핑룸 상태 변경
                updateShoppingRoomStatus(shoppingRoomId);
            }
        } else {
            // 세션에 참여하고 있지 않음
            throw new MemberNotFoundException(memberId);
        }

    }

    /**
     * 쇼핑룸 목록
     **/
    @Override
    public List<ShoppingRoomRes> listShoppingRoom(Long memberId) {
        return shoppingRoomRepository.findShoppingRoomsByMemberId(memberId).orElse(null);
    }

    @Override
    public boolean validatePassword(Long shoppingRoomId, String password) {
        ShoppingRoom shoppingRoom = shoppingRoomRepository.findById(shoppingRoomId).orElseThrow(() -> new ShoppingRoomNotFoundException(shoppingRoomId));

        if (shoppingRoom.getPassword().equals(password))
            return true;

        return false;
    }

    // openvidu 세션 속성 설정
    private ConnectionProperties getConnectionProperties(OpenViduRole role) {
        return new ConnectionProperties.Builder()
                .type(ConnectionType.WEBRTC)
                .role(role)
                .kurentoOptions(
                        new KurentoOptions.Builder()
                                .allowedFilters(new String[]{"GStreamerFilter", "FaceOverlayFilter"})
                                .build())
                .build();
    }

    private String getSessionToken(OpenViduRole role, Long shoppingRoomId, Member member) {
        String token = "";
        try {
            Session session = null;
            // 쇼핑룸 호스트인 경우 openvidu 세션 생성
            if (this.mapSessions.get(shoppingRoomId) == null) {

                session = this.openVidu.createSession();

                // 세션 저장
                log.debug("shoppingRoomId = {}, Session = {} 저장", shoppingRoomId, session);
                this.mapSessions.put(shoppingRoomId, session);
                this.mapSessionNamesTokens.put(shoppingRoomId, new ConcurrentHashMap<>());
            } else {
                session = this.mapSessions.get(shoppingRoomId);
            }

            // 세션 입장 토큰 생성
            token = session.createConnection(getConnectionProperties(role)).getToken();

            // 토큰 저장
            this.mapSessionNamesTokens.get(shoppingRoomId).put(member.getId(), token);
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace();
        }
        return token;
    }

    private void closeSession(Long shoppingRoomId) {
        try {
            this.mapSessions.get(shoppingRoomId).close();
            this.mapSessions.remove(shoppingRoomId);
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void updateShoppingRoomStatus(Long shoppingRoomId) {
        ShoppingRoom shoppingRoom = shoppingRoomRepository.findById(shoppingRoomId).orElseThrow(() -> new ShoppingRoomNotFoundException(shoppingRoomId));
        shoppingRoom.setIsActive(false);
    }
}
