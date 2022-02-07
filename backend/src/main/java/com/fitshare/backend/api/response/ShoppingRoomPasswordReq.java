package com.fitshare.backend.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("ShoppingRoomPasswordRequest")
@Getter
@Setter
public class ShoppingRoomPasswordReq {

    @ApiModelProperty(name = "쇼핑룸 비밀번호", example = "1234")
    private String password;
}
