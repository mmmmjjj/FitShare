package com.fitshare.backend.api.controller;


import com.fitshare.backend.api.response.KakaoLoginRes;
import com.fitshare.backend.api.service.MemberService;
import com.fitshare.backend.common.model.BaseResponseBody;
import com.fitshare.backend.common.model.KakaoProfile;
import com.fitshare.backend.common.auth.KakaoApiService;
import com.fitshare.backend.common.model.RoleType;
import com.fitshare.backend.db.entity.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.fitshare.backend.common.model.ResponseMessage.LOGIN;
import static org.springframework.security.config.Elements.LOGOUT;


@Api(value = "카카오 인증 API", tags = "Kakao")
@RestController
@RequestMapping("/api/v1/kakao")
public class KakaoController {

    @Autowired
    KakaoApiService kakaoApiService;

    @Autowired
    MemberService memberService;

    @GetMapping(value = "/login")
    @ApiOperation(value = "카카오 로그인", notes = "카카오 액세스 토큰으로 유저 정보를 받아 jwt토큰을 발급하고 전송하는 api입니다.")
    public ResponseEntity<BaseResponseBody> login(@RequestParam String accessToken){
        String jwt = null;

        // 1. 인가 코드로 카카오 access 토큰, refresh 토큰 받아오기
        //OAuthToken oAuthToken = kakaoApiService.requestToken(code);

        // 1. access token으로 유저 정보 받아오기
        KakaoProfile kakaoProfile = kakaoApiService.requestUserInfo(accessToken);
        System.out.println(kakaoProfile.toString());

        // 2. DB에서 카카오회원번호로 회원 정보 불러오기
        Long uid = kakaoProfile.getId();

        Optional<Member> member = memberService.findMemberByUid(uid);

        // 3. 없으면 DB에 저장
        if(null==member)
           member = Optional.ofNullable(memberService.createMember(kakaoProfile));

        // 4. JWT token 발급
        jwt = kakaoApiService.createToken(member.get().getId(), RoleType.USER);

        KakaoLoginRes kakaoLoginRes = new KakaoLoginRes(member.get().getId(), jwt, member.get().getName(), member.get().getProfileImg());

        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, LOGIN, kakaoLoginRes));
    }

    @GetMapping(value = "/logout")
    @ApiOperation(value = "카카오 로그아웃",notes = "토큰을 만료 시킨 후 로그아웃한다.")
    public ResponseEntity<BaseResponseBody> logout(@RequestHeader("Authorization") String accessToken) {


        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK,LOGOUT));
    }
}
