package com.fitshare.backend.common.auth;

import com.fitshare.backend.common.model.KakaoProfile;
import com.fitshare.backend.common.model.OAuthToken;
import com.fitshare.backend.common.model.RoleType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoApiService {
    private final JwtTokenProvider tokenProvider;

    // 카카오 client id
    static String clientId = "9596c9c79f92bc2d9fbdeacfee238961";

    // 인가 코드로 카카오에 토큰 요청, 액세스 토큰만 반환
    public String requestToken(String code) {
        String accessToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + clientId +
                    "&redirect_uri=http://localhost:8080/callback" +
                    "&code=" + code;
            bw.write(sb);
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

    public KakaoProfile requestUserInfo(String accessToken){

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

    public String createToken(Long id, RoleType roleType){
        return tokenProvider.createToken(id.toString(),roleType);
    }

}
