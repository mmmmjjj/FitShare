package com.fitshare.backend.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@ApiModel("KakaoLoginResponse")
@Getter
@Setter
@AllArgsConstructor
public class LoginRes {

    @ApiModelProperty(name = "유저 ID", example = "1")
    private Long id;

    @ApiModelProperty(name = "유저 토큰", example = "token값")
    private String token;

    @ApiModelProperty(name = "유저 이름", example = "김싸피")
    private String name;

    @ApiModelProperty(name = "유저 프로필 사진 URI", example = "uri")
    private String profileURI;
}
