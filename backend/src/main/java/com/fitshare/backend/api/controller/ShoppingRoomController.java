package com.fitshare.backend.api.controller;

import com.fitshare.backend.api.request.MakeShoppingRoomReq;
import com.fitshare.backend.api.response.ShoppingRoomPasswordReq;
import com.fitshare.backend.api.service.ShoppingRoomService;
import com.fitshare.backend.common.auth.JwtUtil;
import com.fitshare.backend.common.model.BaseResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.fitshare.backend.common.model.ResponseMessage.*;

@Api(value = "쇼핑룸 API", tags = {"Shopping Room"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shopping-rooms")
public class ShoppingRoomController {

    private final ShoppingRoomService shoppingRoomService;

    @ApiOperation(value = "쇼핑룸 생성", notes = "<strong>쇼핑룸 생성</strong>을 통해 세션과 토큰 생성 후 쇼핑몰 정보와 토큰을 반환한다. <br> => 입력되지 않은 password, shopping mall 정보는 null로 입력하기")
    @PostMapping("")
    public ResponseEntity<BaseResponseBody> makeShoppingRoom(@Valid @RequestBody MakeShoppingRoomReq req) {
        Long memberId = JwtUtil.getCurrentId().orElseThrow(() -> new AccessDeniedException("Access denied"));

        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, MAKE_SHOPPING_ROOM, shoppingRoomService.makeShoppingRoom(memberId, req)));
    }

    @ApiOperation(value = "쇼핑룸 입장", notes = "<strong>입장하기</strong>를 통해 참가자가 쇼핑룸을 입장 후 쇼핑몰 정보와 토큰을 반환한다.")
    @GetMapping("/{shoppingRoomId}")
    public ResponseEntity<BaseResponseBody> enterShoppingRoom(@PathVariable Long shoppingRoomId) {
        Long memberId = JwtUtil.getCurrentId().orElseThrow(() -> new AccessDeniedException("Access denied"));

        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK, ENTER_SHOPPING_ROOM, shoppingRoomService.enterShoppingRoom(memberId, shoppingRoomId)));
    }

    @ApiOperation(value = "쇼핑룸 나가기", notes = "<strong>쇼핑룸 나기기</strong>를 통해 쇼핑룸 비활성화하고 세션 정보를 삭제한다.")
    @PostMapping("/{shoppingRoomId}")
    public ResponseEntity<BaseResponseBody> exitShoppingRoom(@PathVariable Long shoppingRoomId) {
        Long memberId = JwtUtil.getCurrentId().orElseThrow(() -> new AccessDeniedException("Access denied"));

        shoppingRoomService.exitShoppingRoom(memberId, shoppingRoomId);
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK, EXIT_SHOPPING_ROOM));
    }

    @ApiOperation(value = "쇼핑몰 목록", notes = "사용자의 친구가 라이브 중인 쇼핑몰 목록을 반환한다.")
    @GetMapping("")
    public ResponseEntity<BaseResponseBody> listShoppingRoom() {
        Long memberId = JwtUtil.getCurrentId().orElseThrow(() -> new AccessDeniedException("Access denied"));

        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK, GET_SHOPPING_ROOM_LIST, shoppingRoomService.listShoppingRoom(memberId)));
    }

    @ApiOperation(value = "쇼핑룸 비밀번호 확인", notes = "비밀방 입장시 비밀번호를 확인한다.")
    @PostMapping("/{shoppingRoomId}/validate")
    public ResponseEntity<BaseResponseBody> validatePassword(@PathVariable Long shoppingRoomId, @RequestBody ShoppingRoomPasswordReq req) {
        boolean res = shoppingRoomService.validatePassword(shoppingRoomId, req.getPassword());

        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK, res ? MATCH_SHOPPING_ROOM_PASSWORD : NOT_MATCH_SHOPPING_ROOM_PASSWORD, res));
    }
}
