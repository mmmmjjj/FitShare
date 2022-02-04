package com.fitshare.backend.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("ShoppingRoomTokenResponse")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingRoomTokenRes {

    @ApiModelProperty(name = "쇼핑룸 Id", example = "1")
    private Long shoppingRoomId;

    @ApiModelProperty(name = "쇼핑룸 이름", example = "쇼핑룸 이름")
    private String shoppingRoomName;

    @ApiModelProperty(name = "쇼핑룸 url", example = "www.xxx.com")
    private String shoppingRoomUrl;

    @ApiModelProperty(name = "쇼핑룸 입장 토큰", example = "token")
    private String token;
}
