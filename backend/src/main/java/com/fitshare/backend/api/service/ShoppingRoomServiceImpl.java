package com.fitshare.backend.api.service;

import com.fitshare.backend.api.request.MakeShoppingRoomReq;
import com.fitshare.backend.api.response.ShoppingRoomRes;
import com.fitshare.backend.api.response.ShoppingRoomTokenRes;
import com.fitshare.backend.common.exception.*;
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
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
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

    Map<String, Integer> participants = new ConcurrentHashMap<>();

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

        // openvidu 세션 생성
        Session session = createSession();
        // 토큰 생성
        String sessionToken = getSessionToken(session, OpenViduRole.PUBLISHER);

        ShoppingRoom shoppingRoom = ShoppingRoom.builder()
                .host(member)
                .shoppingMallName(req.getShoppingMallName())
                .shoppingMallUrl(req.getShoppingMallUrl())
                .participantCount(req.getParticipantCount())
                .password(req.getPassword())
                .isPrivate(req.isPrivate())
                .isCustomShoppingMall(req.isCustomShoppingMall())
                .isActive(true)
                .sessionId(session.getSessionId())
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

        Session session = getSession(shoppingRoom.getSessionId());
        String sessionToken = getSessionToken(session, OpenViduRole.PUBLISHER);

        // 입장 가능 인원 확인, 참여 인원이 같거나 많은 경우 인원 초과 예외 발생
        if (shoppingRoom.getParticipantCount() <= session.getConnections().size()) {
            throw new ExceedParticipantCountException();
        }

        // 이미 입장했던 사용자인지 확인
        if (!checkParticipant(memberId, shoppingRoomId)) {
            // 처음 입장하는 사용자이면 참여자 DB 저장
            roomParticipantRepository.save(new RoomParticipant(shoppingRoom, member));
        }

        return new ShoppingRoomTokenRes(shoppingRoomId, shoppingRoom.getShoppingMallName(), shoppingRoom.getShoppingMallUrl(), sessionToken);
    }

    /**
     * 쇼핑룸 나가기
     **/
    @Transactional
    @Override
    public void exitShoppingRoom(Long memberId, Long shoppingRoomId) {

        ShoppingRoom shoppingRoom = shoppingRoomRepository.findById(shoppingRoomId).orElseThrow(() -> new ShoppingRoomNotFoundException(shoppingRoomId));

        if (!checkParticipant(memberId, shoppingRoomId))
            throw new ParticipantNotFoundException(memberId, shoppingRoomId);

        if (participants.get(shoppingRoom.getSessionId()) == 1) {
            participants.remove(shoppingRoom.getSessionId());
            updateShoppingRoomStatus(shoppingRoomId, false);
        } else {
            participants.put(shoppingRoom.getSessionId(), participants.get(shoppingRoom.getSessionId()) - 1);
        }
    }

    /**
     * 쇼핑룸 목록
     **/
    @Override
    public List<ShoppingRoomRes> listShoppingRoom(Long memberId) {
        List<ShoppingRoomRes> shoppingRoomRes = shoppingRoomRepository.findShoppingRoomsByMemberId(memberId);

        for (ShoppingRoomRes shoppingRoom : shoppingRoomRes) {
            shoppingRoom.setParticipantCount(participants.get(shoppingRoom.getSessionId()));
        }

        return shoppingRoomRes;
    }

    @Override
    public boolean validatePassword(Long shoppingRoomId, String password) {
        ShoppingRoom shoppingRoom = shoppingRoomRepository.findById(shoppingRoomId).orElseThrow(() -> new ShoppingRoomNotFoundException(shoppingRoomId));

        if (shoppingRoom.getPassword().equals(password))
            return true;
        return false;
    }

    @Transactional
    public void updateShoppingRoomStatus(Long shoppingRoomId, boolean status) {
        ShoppingRoom shoppingRoom = shoppingRoomRepository.findById(shoppingRoomId).orElseThrow(() -> new ShoppingRoomNotFoundException(shoppingRoomId));
        shoppingRoom.setIsActive(status);
    }

    private Session createSession() {
        Session session = null;
        try {
            session = this.openVidu.createSession();
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace();
        }
        return session;
    }

    private Session getSession(String sessionId) {
        String url = "https://" + OPENVIDU_URL + "/openvidu/api/sessions/" + sessionId;

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(SECRET.getBytes()));

        HttpEntity<Session> entity = new HttpEntity<>(headers);

        ResponseEntity<Session> response = restTemplate.exchange(url, HttpMethod.GET, entity, Session.class);

        if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            throw new SessionNotFoundException(sessionId);

        return response.getBody();
    }

    private String getSessionToken(Session session, OpenViduRole role) {
        String token = "";
        try {
            // 세션 입장 토큰 생성
            token = session.createConnection(getConnectionProperties(role)).getToken();

            participants.put(session.getSessionId(), participants.getOrDefault(session.getSessionId(), 0) + 1);
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace();
        }
        return token;
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

    private boolean checkParticipant(Long memberId, Long shoppingRoomId) {
        Optional<RoomParticipant> roomParticipant = roomParticipantRepository.findByMemberIdAndShoppingRoomId(memberId, shoppingRoomId);

        if (roomParticipant.isPresent())
            return true;

        return false;
    }
}
