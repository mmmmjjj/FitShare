package com.fitshare.backend.common.model;

public class ResponseMessage {
    private ResponseMessage() {
    }

    // Shopping Room
    public static final String MAKE_SHOPPING_ROOM = "쇼핑룸 생성 성공입니다.";
    public static final String ENTER_SHOPPING_ROOM = "쇼핑룸 입장 성공입니다.";
    public static final String EXIT_SHOPPING_ROOM = "쇼핑룸 나가기 성공입니다.";
    public static final String GET_SHOPPING_LIST = "쇼핑룸 목록 조회 성공입니다.";

    // Shopping Mall
    public static final String SEARCH_SHOPPING_MALL = "쇼핑몰 검색 성공입니다.";

    // Login
    public static final String GET_ACCESS_TOKEN = "카카오 토큰 생성 성공입니다.";
    public static final String LOGIN = "로그인 성공입니다.";
    public static final String LOGOUT = "로그아웃 성공입니다.";

    // Cloth
    public static final String ADD_CLOTH = "옷 추가 성공입니다.";
    public static final String GET_CLOTHES_LIST = "옷 목록 조회 성공입니다.";
    public static final String DELETE_CLOTH = "옷 삭제 성공입니다.";

    // Member
    public static final String SEARCH_MEMBERS_BY_EMAIL = "이메일로 사용자 조회 성공입니다.";

    // Friend
    public static final String ADD_FRIEND = "친구 추가 성공입니다.";
    public static final String GET_FRIEND_LIST = "친구 목록 조회 성공입니다.";
    public static final String GET_FRIEND_LIST_BY_EMAIL = "친구 검색 성공입니다.";
    public static final String DELETE_FRIEND = "친구 삭제 성공입니다.";
    public static final String ADD_FRIEND_REQUEST = "친구 요청 성공입니다.";
    public static final String GET_FRIEND_REQUEST_LIST = "친구 요청 목록 조회 성공입니다.";
    public static final String DELETE_FRIEND_REQUEST = "친구 요청 삭제 성공입니다.";
}
