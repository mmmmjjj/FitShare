package com.fitshare.backend.api.service;

import com.fitshare.backend.api.response.TokenRes;
import com.fitshare.backend.common.model.KakaoProfile;
import com.fitshare.backend.common.model.NaverProfile;
import com.fitshare.backend.common.model.RoleType;

public interface AuthService {
    String getKakaoAccessToken(String code);

    KakaoProfile getKakaoUserInfo(String accessToken);

    String getNaverAccessToken(String code, String state);

    NaverProfile getNaverUserInfo(String accessToken);

    String createToken(Long id, RoleType roleType);

    String createRefreshToken(Long id);

    TokenRes refreshAccessToken(String refreshToken);
}
