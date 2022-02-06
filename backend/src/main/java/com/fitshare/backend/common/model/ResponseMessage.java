package com.fitshare.backend.common.model;

public class ResponseMessage {
    private ResponseMessage() {
    }

    // Shopping Room
    public static final String MAKE_SHOPPING_ROOM = "쇼핑룸 생성 성공입니다.";
    public static final String ENTER_SHOPPING_ROOM = "쇼핑룸 입장 성공입니다.";
    public static final String EXIT_SHOPPING_ROOM = "쇼핑룸 나가기 성공입니다.";
    public static final String GET_SHOPPING_LIST = "쇼핑룸 목록 조회 성공입니다.";

    // Login
    public static final String LOGIN = "로그인 되었습니다.";
    public static final String LOGOUT = "로그아웃 되었습니다.";

    // Friend
    public static final String ADD_FRIEND = "친구 추가 성공입니다.";
    public static final String GET_FRIEND_LIST = "친구 목록 조회 성공입니다.";
    public static final String GET_FRIEND_LIST_BY_EMAIL = "친구 검색 성공입니다.";
    public static final String DELETE_FRIEND = "친구 삭제 성공입니다.";
    public static final String ADD_FRIEND_REQUEST = "친구 요청 성공입니다.";
    public static final String GET_FRIEND_REQUEST_LIST = "친구 요청 목록 조회 성공입니다.";
    public static final String DELETE_FRIEND_REQUEST = "친구 요청 삭제 성공입니다.";
}
