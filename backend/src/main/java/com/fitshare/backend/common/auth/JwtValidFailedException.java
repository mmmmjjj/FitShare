package com.fitshare.backend.common.auth;

public class JwtValidFailedException extends RuntimeException {

    public JwtValidFailedException() {
        super("토큰 생성을 실패했습니다.");
    }

    private JwtValidFailedException(String message) {
        super(message);
    }
}
