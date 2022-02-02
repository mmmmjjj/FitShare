package com.fitshare.backend.api.controller;

import com.fitshare.backend.api.request.MakeShoppingRoomReq;
import com.fitshare.backend.api.response.MakeShoppingRoomRes;
import com.fitshare.backend.api.service.ShoppingRoomService;
import com.fitshare.backend.common.model.BaseResponseBody;
import com.fitshare.backend.common.model.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fitshare.backend.common.model.ResponseMessage.*;

@Api(value = "쇼핑룸 API", tags = {"Shopping Room"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shopping-rooms")
public class ShoppingRoomController {

    private final ShoppingRoomService shoppingRoomService;

    @ApiOperation(value = "쇼핑룸 생성")
    @PostMapping("")
    public ResponseEntity<BaseResponseBody> makeRoom(@RequestBody MakeShoppingRoomReq req) {
        // TODO: 사용자 정보 얻기
        Long memberId = 1L;

        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, MAKE_SHOPPING_ROOM, shoppingRoomService.makeShoppingRoom(memberId, req)));
    }
}
