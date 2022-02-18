package com.fitshare.backend.common.model;

import lombok.Data;

// 카카오에게 response 받는 토큰
@Data
public class OAuthToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;
}
