package com.fitshare.backend.api.service;

import com.fitshare.backend.api.response.TokenRes;
import com.fitshare.backend.common.auth.JwtTokenProvider;
import com.fitshare.backend.common.model.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider tokenProvider;
    private final RedisService redisService;

    // 카카오 client id
    private final String KAKAO_CLIENT_ID;

    // 네이버 client id
    private final String NAVER_CLIENT_ID;
    private final String NAVER_CLIENT_SCECRET;

    public AuthServiceImpl(JwtTokenProvider tokenProvider,
                           RedisService redisService, @Value("${kakao.client.id}") String kakaoClientId,
                           @Value("${naver.client.id}") String naverClientId,
                           @Value("${naver.client.secret}") String naverClientSecret) {
        this.tokenProvider = tokenProvider;
        this.redisService = redisService;

        this.KAKAO_CLIENT_ID = kakaoClientId;
        this.NAVER_CLIENT_ID = naverClientId;
        this.NAVER_CLIENT_SCECRET = naverClientSecret;
    }

    // 인가 코드로 카카오에 토큰 요청, 액세스 토큰만 반환
    @Override
    public String getKakaoAccessToken(String code) {
        String reqURL = "https://kauth.kakao.com/oauth/token";

        String param = "grant_type=authorization_code" +
                "&client_id=" + KAKAO_CLIENT_ID +
                "&redirect_uri=https://i6a405.p.ssafy.io/callback" +
                "&code=" + code;
        return getAccessToken(reqURL, param);
    }

    @Override
    public KakaoProfile getKakaoUserInfo(String accessToken){

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        //HttpHeader
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization","Bearer " + accessToken);

        //HttpHeader 담기
        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                KakaoProfile.class).getBody();
    }

    @Override
    public String getNaverAccessToken(String code, String state) {
        String reqURL = "https://nid.naver.com/oauth2.0/token";

        String param = "grant_type=authorization_code" +
                "&client_id=" + NAVER_CLIENT_ID +
                "&client_secret=" + NAVER_CLIENT_SCECRET +
                "&code=" + code +
                "&state=" + state;

        return getAccessToken(reqURL, param);
    }

    @Override
    public NaverProfile getNaverUserInfo(String accessToken){

        String apiURL = "https://openapi.naver.com/v1/nid/me";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        //HttpHeader
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization","Bearer " + accessToken);

        //HttpHeader 담기
        HttpEntity<MultiValueMap<String,String>> naverProfileRequest = new HttpEntity<>(headers);

        return restTemplate.exchange(
                apiURL,
                HttpMethod.GET,
                naverProfileRequest,
                NaverProfile.class).getBody();
    }

    // accessToken 발급
    @Override
    public String createToken(Long id, RoleType roleType){

        return tokenProvider.createToken(id.toString(),roleType);
    }

    // refreshToken 발급
    @Override
    public String createRefreshToken(Long id){
        String refreshToken = tokenProvider.createRefreshToken();

        //Redis에 저장
        redisService.setData(refreshToken, String.valueOf(id));
        return refreshToken;
    }

    // refreshToken으로 accessToken 재발급
    public TokenRes refreshAccessToken(String refreshToken) {

        String id = (String) redisService.getData(refreshToken);

        return new TokenRes(tokenProvider.createToken(id,RoleType.USER),refreshToken);
    }

    private String getAccessToken(String reqURL, String param) {
        String accessToken = "";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(param);
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
                System.out.println(line);
                log.info(line);
            }
            JsonElement jsonElement = JsonParser.parseString(result.toString());
            accessToken = jsonElement.getAsJsonObject().get("access_token").getAsString();
            log.info(accessToken);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessToken;
    }


}
