package com.fitshare.backend.api.service;

import com.fitshare.backend.api.request.MakeShoppingRoomReq;
import com.fitshare.backend.api.response.MakeShoppingRoomRes;
import com.fitshare.backend.common.exception.MemberNotFoundException;
import com.fitshare.backend.common.exception.ShoppingMallNotFoundException;
import com.fitshare.backend.db.entity.Member;
import com.fitshare.backend.db.entity.ShoppingMall;
import com.fitshare.backend.db.entity.ShoppingRoom;
import com.fitshare.backend.db.repository.MemberRepository;
import com.fitshare.backend.db.repository.ShoppingMallRepository;
import com.fitshare.backend.db.repository.ShoppingRoomRepository;
import io.openvidu.java.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingRoomServiceImpl implements ShoppingRoomService {

    private MemberRepository memberRepository;
    private ShoppingRoomRepository shoppingRoomRepository;
    private ShoppingMallRepository shoppingMallRepository;

    // openvidu 객체
    private OpenVidu openVidu;

    // openvidu server 관련 변수
    private String OPENVIDU_URL;
    private String SECRET;

    public ShoppingRoomServiceImpl(MemberRepository memberRepository, ShoppingRoomRepository shoppingRoomRepository, ShoppingMallRepository shoppingMallRepository,
                                   @Value("${openvidu.url}") String openviduUrl, @Value("${openvidu.secret}") String secret) {
        this.memberRepository = memberRepository;
        this.shoppingRoomRepository = shoppingRoomRepository;
        this.shoppingMallRepository = shoppingMallRepository;

        this.OPENVIDU_URL = openviduUrl;
        this.SECRET = secret;
        this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
    }

    @Transactional
    @Override
    public MakeShoppingRoomRes makeShoppingRoom(Long memberId, MakeShoppingRoomReq req) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

        ShoppingRoom shoppingRoom = ShoppingRoom.builder()
                .host(member)
                .participantCount(req.getParticipantCount())
                .isPrivate(req.isPrivate())
                .isCustomShoppingMall(req.isCustomShoppingMall())
                .isActive(true)
                .build();

        if (req.isCustomShoppingMall()) {
            shoppingRoom.setShoppingMallName(req.getShoppingMallName());
            shoppingRoom.setShoppingMallUrl(req.getShoppingMallUrl());
        } else {
            ShoppingMall shoppingMall = shoppingMallRepository.findById(req.getShoppingMallId())
                    .orElseThrow(() -> new ShoppingMallNotFoundException(req.getShoppingMallId()));
            shoppingRoom.setShoppingMall(shoppingMall);
        }

        if (req.isPrivate())
            shoppingRoom.setPassword(req.getPassword());

        String token = "";
        try {
            // openvidu 세션 생성
            Session session = this.openVidu.createSession();

            // 세션 입장 토큰 생성
           token = session.createConnection(getConnectionProperties(OpenViduRole.PUBLISHER)).getToken();

            // 쇼핑룸 정보 DB 저장
            shoppingRoomRepository.save(shoppingRoom);
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace();
        }

        return new MakeShoppingRoomRes(shoppingRoom.getId(), token);
    }

    // openvidu 세션 속성 설정
    private ConnectionProperties getConnectionProperties(OpenViduRole role) {
        return new ConnectionProperties.Builder()
                .type(ConnectionType.WEBRTC)
                .role(role)
                .build();
    }
}
