package com.fitshare.backend.api.controller;


import com.fitshare.backend.api.response.LoginRes;
import com.fitshare.backend.api.response.TokenRes;
import com.fitshare.backend.api.service.AuthService;
import com.fitshare.backend.api.service.MemberService;
import com.fitshare.backend.api.service.RedisService;
import com.fitshare.backend.common.auth.JwtTokenProvider;
import com.fitshare.backend.common.exception.RefreshInvalidException;
import com.fitshare.backend.common.exception.handler.ErrorResponse;
import com.fitshare.backend.common.model.BaseResponseBody;
import com.fitshare.backend.common.model.KakaoProfile;
import com.fitshare.backend.common.model.NaverProfile;
import com.fitshare.backend.common.model.RoleType;
import com.fitshare.backend.db.entity.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jdk.nashorn.internal.parser.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.fitshare.backend.common.model.ResponseMessage.*;

@Api(value = "소셜 로그인 API", tags = "Auth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final RedisService redisService;
    private final JwtTokenProvider tokenProvider;

    @ApiOperation(value = "카카오 토큰 요청", notes = "카카오 인가 코드로 액세스 토큰을 요청하는 api입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = GET_KAKAO_ACCESS_TOKEN),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @GetMapping(value="/kakao/token")
    public ResponseEntity<BaseResponseBody> requestKakaoToken(@RequestParam String code){
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK,GET_KAKAO_ACCESS_TOKEN, authService.getKakaoAccessToken(code)));
    }

    @ApiOperation(value = "카카오 로그인", notes = "카카오 액세스 토큰으로 유저 정보를 받아 jwt 토큰을 발급하고 전송하는 api입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = LOGIN, response = LoginRes.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @GetMapping(value = "/kakao/login")
    public ResponseEntity<BaseResponseBody> login(@RequestParam String accessToken){
        // 1. access token으로 유저 정보 받아오기
        KakaoProfile kakaoProfile = authService.getKakaoUserInfo(accessToken);
        System.out.println(kakaoProfile.toString());

        // 2. DB에서 카카오회원번호로 회원 정보 불러오기
        String uid = Long.toString(kakaoProfile.getId());

        Member member = memberService.findMemberByUid(uid).orElse(null);

        // 3. 없으면 DB에 저장
        if(member == null)
           member = memberService.createKakaoMember(kakaoProfile);
        else {
            String profileUrl = kakaoProfile.getKakao_account().getProfile().getProfile_image_url();
            if(!profileUrl.equals(member.getProfileImg()))
                memberService.updateProfileImage(member,profileUrl);
        }


        // 4. JWT token 발급
        String token = authService.createToken(member.getId(), RoleType.USER);
        String refreshToken = authService.createRefreshToken(member.getId());

        LoginRes loginRes = new LoginRes(member.getId(), token, refreshToken, member.getName(), member.getProfileImg());


        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, LOGIN, loginRes));
    }

    @ApiOperation(value = "로그아웃",notes = "토큰을 만료 시킨 후 로그아웃한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = LOGOUT),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @GetMapping(value = "/logout")
    public ResponseEntity<BaseResponseBody> logout(@RequestParam String refreshToken) {
        // 레디스에서 해당 아이디 refreshToken 삭제
        redisService.delData(refreshToken);
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK,LOGOUT));
    }

    @ApiOperation(value = "네이버 토큰 요청", notes = "네이버 인가 코드로 액세스 토큰을 요청하는 api입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = GET_NAVER_ACCESS_TOKEN),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @GetMapping(value="/naver/token")
    public ResponseEntity<BaseResponseBody> requestNaverToken(@RequestParam String code, @RequestParam String state){
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK,GET_NAVER_ACCESS_TOKEN, authService.getNaverAccessToken(code, state)));
    }

    @ApiOperation(value = "네이버 로그인", notes = "네이버 액세스 토큰으로 유저 정보를 받아 jwt 토큰을 발급하고 전송하는 api입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = LOGIN, response = LoginRes.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @GetMapping(value = "/naver/login")
    public ResponseEntity<BaseResponseBody> naverLogin(@RequestParam String accessToken){

        NaverProfile naverProfile = authService.getNaverUserInfo(accessToken);

        String uid = naverProfile.getResponse().getId();

        Member member = memberService.findMemberByUid(uid).orElse(null);

        if(member == null)
            member = memberService.createNaverMember(naverProfile);

        String token = authService.createToken(member.getId(), RoleType.USER);
        String refreshToken = authService.createRefreshToken(member.getId());

        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, LOGIN, new LoginRes(member.getId(), token,refreshToken, member.getName(), member.getProfileImg())));
    }

    @ApiOperation(value = "토큰 재발급 요청", notes = "만료된 accessToken을 refreshToken을 통해 재발급하는 api입니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = REFRESH_TOKEN, response = TokenRes.class),
            @ApiResponse(code = 403, message = INVALID_TOKEN, response = ErrorResponse.class)
    })
    @PostMapping(value = "/refresh")
    public ResponseEntity<BaseResponseBody> refreshToken(@RequestParam String refreshToken){
        if(!tokenProvider.validateToken(refreshToken))
            throw new RefreshInvalidException();

        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED,REFRESH_TOKEN,authService.refreshAccessToken(refreshToken)));
    }
}
