package com.fitshare.backend.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@ApiModel("ShoppingRoomResponse")
@Getter
@Setter
@AllArgsConstructor
public class ShoppingRoomRes {

    @ApiModelProperty(name = "호스트 이름", example = "홍길동")
    private String hostName;

    @ApiModelProperty(name = "쇼핑룸 Id", example = "1")
    private Long shoppingRoomId;

    @ApiModelProperty(name = "쇼핑룸 이름", example = "쇼핑몰 이름")
    private String shoppingMallName;

    @ApiModelProperty(name = "쇼핑룸 url", example = "www.xxx.com")
    private String shoppingMallUrl;

    @ApiModelProperty(name = "공개 여부", example = "true")
    private Boolean isPrivate;

    @ApiModelProperty(name = "입장 가능한 인원", example = "6")
    private int maxParticipantCount;

    @ApiModelProperty(name = "입장한 인원", example = "1")
    private Long participantCount;
}
