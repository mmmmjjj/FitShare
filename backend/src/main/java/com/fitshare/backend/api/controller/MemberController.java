package com.fitshare.backend.api.controller;


import com.fitshare.backend.api.service.MemberService;
import com.fitshare.backend.common.model.BaseResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.fitshare.backend.common.model.ResponseMessage.SEARCH_MEMBERS_BY_EMAIL;

@Api(value = "회원 API", tags = "Member")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "사용자 검색", notes = "가입한 사용자 전체 리스트에서 이메일로 사용자를 검색합니다.")
    @GetMapping("/{email}")
    public ResponseEntity<BaseResponseBody> searchMembersByEmail(@PathVariable String email) {
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK, SEARCH_MEMBERS_BY_EMAIL, memberService.searchMembersByEmail(email)));
    }

}
