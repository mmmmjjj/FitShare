package com.fitshare.backend.api.controller;

import com.fitshare.backend.api.request.MakeShoppingRoomReq;
import com.fitshare.backend.api.service.ShoppingRoomService;
import com.fitshare.backend.common.auth.JwtUtil;
import com.fitshare.backend.common.model.BaseResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.fitshare.backend.common.model.ResponseMessage.*;

@Api(value = "쇼핑룸 API", tags = {"Shopping Room"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shopping-rooms")
public class ShoppingRoomController {

    private final ShoppingRoomService shoppingRoomService;

    @ApiOperation(value = "쇼핑룸 생성", notes = "<strong>쇼핑룸 생성</strong>을 통해 세션과 토큰을 생성 후 쇼핑몰 정보와 토큰 반환 => 입력되지 않은 password, shopping mall 정보는 null로 입력하기")
    @PostMapping("")
    public ResponseEntity<BaseResponseBody> makeShoppingRoom(@RequestBody MakeShoppingRoomReq req) {
        Long memberId = JwtUtil.getCurrentId().get();

        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, MAKE_SHOPPING_ROOM, shoppingRoomService.makeShoppingRoom(memberId, req)));
    }

    @ApiOperation(value = "쇼핑룸 입장", notes = "<strong>입장하기</strong>를 통해 참가자가 쇼핑몰을 입장 후 쇼핑몰 정보와 토큰 반환")
    @GetMapping("/{shoppingRoomId}")
    public ResponseEntity<BaseResponseBody> enterShoppingRoom(@PathVariable Long shoppingRoomId) {
        Long memberId = JwtUtil.getCurrentId().get();

        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK, ENTER_SHOPPING_ROOM, shoppingRoomService.enterShoppingRoom(memberId, shoppingRoomId)));
    }

    @ApiOperation(value = "쇼핑룸 나가기", notes = "<strong>쇼핑룸 나기기</strong>를 통해 쇼핑룸 비활성화 및 세션 정보 삭제")
    @PostMapping("/{shoppingRoomId}")
    public ResponseEntity<BaseResponseBody> exitShoppingRoom(@PathVariable Long shoppingRoomId) {
        Long memberId = JwtUtil.getCurrentId().get();

        shoppingRoomService.exitShoppingRoom(memberId, shoppingRoomId);
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK, EXIT_SHOPPING_ROOM));
    }

    @ApiOperation(value = "쇼핑몰 목록", notes = "사용자의 친구가 라이브 중인 쇼핑몰 목록 반환")
    @GetMapping("")
    public ResponseEntity<BaseResponseBody> listShoppingRoom() {
        Long memberId = JwtUtil.getCurrentId().get();

        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK, GET_SHOPPING_LIST, shoppingRoomService.listShoppingRoom(memberId)));
    }
}
