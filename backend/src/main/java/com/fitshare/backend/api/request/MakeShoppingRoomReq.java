package com.fitshare.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("MakeRoomRequest")
@Getter
@Setter
public class MakeShoppingRoomReq {

    @ApiModelProperty(name = "쇼핑몰 Id", example = "1")
    private Long shoppingMallId;

    @ApiModelProperty(name = "쇼핑몰 이름", example = "쇼핑몰이름")
    private String shoppingMallName;

    @ApiModelProperty(name = "쇼핑몰 url", example = "www.xxx.com")
    private String shoppingMallUrl;

    @ApiModelProperty(name = "인원수", example = "2")
    private int participantCount;

    @ApiModelProperty(name = "비밀번호", example = "1234")
    private String password;

    @ApiModelProperty(name = "공개여부", example = "false")
    private boolean isPrivate;

    @ApiModelProperty(name = "쇼핑몰 직접 입력", example = "false")
    private boolean isCustomShoppingMall;
}
