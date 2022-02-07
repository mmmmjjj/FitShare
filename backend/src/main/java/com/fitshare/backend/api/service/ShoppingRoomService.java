package com.fitshare.backend.api.service;

import com.fitshare.backend.api.request.MakeShoppingRoomReq;
import com.fitshare.backend.api.response.ShoppingRoomRes;
import com.fitshare.backend.api.response.ShoppingRoomTokenRes;

import java.util.List;

public interface ShoppingRoomService {

    ShoppingRoomTokenRes makeShoppingRoom(Long memberId, MakeShoppingRoomReq req);

    ShoppingRoomTokenRes enterShoppingRoom(Long memberId, Long shoppingRoomId);

    void exitShoppingRoom(Long memberId, Long shoppingRoomId);

    List<ShoppingRoomRes> listShoppingRoom(Long memberId);

    boolean validatePassword(Long shoppingRoomId, String password);
}
