package com.fitshare.backend.api.service;

import com.fitshare.backend.api.request.MakeShoppingRoomReq;
import com.fitshare.backend.api.response.MakeShoppingRoomRes;

public interface ShoppingRoomService {
    MakeShoppingRoomRes makeShoppingRoom(Long memberId, MakeShoppingRoomReq req) ;
}
