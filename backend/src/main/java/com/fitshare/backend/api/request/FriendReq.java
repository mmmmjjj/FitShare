package com.fitshare.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("FriendRequest")
@Setter
@Getter
public class FriendReq {

    @ApiModelProperty(name = "친구 Id", example = "1")
    private Long friendId;

}
