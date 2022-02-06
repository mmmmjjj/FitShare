package com.fitshare.backend.api.controller;

import com.fitshare.backend.api.request.FriendReq;
import com.fitshare.backend.api.service.FriendService;
import com.fitshare.backend.common.model.BaseResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.fitshare.backend.common.model.ResponseMessage.*;


@Api(value = "친구 API", tags = {"Friend"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/friends")
public class FriendController {

    private final FriendService friendService;

    @ApiOperation(value = "친구 추가", notes = "친구 고유 ID로 친구를 추가합니다. 친구 요청을 수락하면 사용합니다.")
    @PostMapping("")
    public ResponseEntity<BaseResponseBody> addFriend(@RequestBody FriendReq friendReq) {
        friendService.addFriend(friendReq);
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, ADD_FRIEND));
    }

    @ApiOperation(value = "친구 목록 조회", notes = "사용자의 친구 목록을 조회합니다.")
    @GetMapping("")
    public ResponseEntity<BaseResponseBody> getFriendList() {
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, GET_FRIEND_LIST, friendService.getFriendList()));
    }

    @ApiOperation(value = "친구 검색", notes = "친구 리스트에서 이메일로 친구를 검색합니다.")
    @GetMapping("/{friendEmail}")
    public ResponseEntity<BaseResponseBody> getFriendListByEmail(@PathVariable String friendEmail) {
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, GET_FRIEND_LIST_BY_EMAIL, friendService.getFriendListByEmail(friendEmail)));
    }

    @ApiOperation(value = "친구 삭제", notes = "사용자 고유 ID와 삭제할 친구 고유 ID로 친구를 삭제합니다.")
    @DeleteMapping("/{friendId}")
    public ResponseEntity<BaseResponseBody> deleteFriend(@PathVariable Long friendId) {
        friendService.deleteFriend(friendId);
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, DELETE_FRIEND));
    }

    @ApiOperation(value = "친구 요청", notes = "사용자 고유 ID와 친구요청을 받을 사용자의 고유 ID로 친구를 요청합니다.")
    @PostMapping("/requests")
    public ResponseEntity<BaseResponseBody> addFriendRequest(@RequestBody FriendReq friendReq) {
        friendService.addFriendRequest(friendReq);
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, ADD_FRIEND_REQUEST));
    }

    @ApiOperation(value = "친구 요청 목록 조회", notes = "사용자 고유 ID로 받은 친구 요청 목록을 조회합니다.")
    @GetMapping("/requests")
    public ResponseEntity<BaseResponseBody> getFriendRequestList() {
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, GET_FRIEND_REQUEST_LIST, friendService.getFriendRequestList()));
    }

    @ApiOperation(value = "친구 요청 삭제", notes = "사용자 고유 ID와 친구요청을 받은 친구의 고유 ID로 친구 요청을 삭제합니다.")
    @DeleteMapping("/requests/{requesterId}")
    public ResponseEntity<BaseResponseBody> deleteFriendRequest(@PathVariable Long requesterId) {
        friendService.deleteFriendRequest(requesterId);
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, DELETE_FRIEND_REQUEST));
    }

}

