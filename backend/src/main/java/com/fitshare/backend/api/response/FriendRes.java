package com.fitshare.backend.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@ApiModel("FriendResponse")
@AllArgsConstructor
@Setter
@Getter
public class FriendRes {

    @ApiModelProperty(name = "친구 Id", example = "1")
    private Long id;

    @ApiModelProperty(name = "친구 이름", example = "fitshare")
    private String name;

    @ApiModelProperty(name = "친구 이메일", example = "fitshare@fitshare.com")
    private String email;

    @ApiModelProperty(name = "친구 전화번호", example = "010-1234-5678")
    private String phone;

    @ApiModelProperty(name = "친구 프로필 이미지", example = "http://k.kakaocdn.net/dn/")
    private String profileImg;

    @ApiModelProperty(name = "친구 추가 시각", example = "2022-02-03T17:20:52")
    private LocalDateTime createdTime;

}
