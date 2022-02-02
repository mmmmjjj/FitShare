package com.fitshare.backend.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@ApiModel("MakeRoomResponse")
@Getter
@Setter
@AllArgsConstructor
public class MakeShoppingRoomRes {

    @ApiModelProperty(name = "쇼핑룸 Id", example = "1")
    private Long roomId;

    @ApiModelProperty(name = "쇼핑룸 입장 토큰", example = "token")
    private String token;
}
