package com.fitshare.backend.api.controller;


import com.fitshare.backend.api.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Api(value = "회원 API", tags = "Member")
@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    @Autowired
    MemberService memberService;


    // 프로필 사진 변경
//    @PutMapping("/image")
//    @ApiOperation(value="프로필 사진 변경", notes = "유저의 프로필 사진을 변경합니다.")
//    public ResponseEntity<> changeProfileImage(MultipartHttpServletRequest request){
//
//
//        return ResponseEntity.ok("프로필 사진을 수정하였습니다.");
//    }

    // 회원 정보 수정


    // 회원 탈퇴

}
