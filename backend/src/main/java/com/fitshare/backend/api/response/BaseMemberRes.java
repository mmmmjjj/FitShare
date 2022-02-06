package com.fitshare.backend.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel("MemberResponse")
@Getter
@Setter
@AllArgsConstructor
public class BaseMemberRes {

    @ApiModelProperty(name = "사용자 고유값", example = "1")
    private Long id;

    @ApiModelProperty(name = "이름", example = "홍길동")
    private String name;

    @ApiModelProperty(name = "프로필 이미지", example = "http://k.kakaocdn.net/dn/example.jpg")
    private String profileImg;

    @ApiModelProperty(name = "이메일", example = "example@gmail.com")
    private String email;

}
