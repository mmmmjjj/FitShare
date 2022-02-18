package com.fitshare.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@ApiModel("ClothRequest")
@Getter
@Setter
public class ClothReq {

    @ApiModelProperty(name = "쇼핑룸 Id", example = "1")
    private Long shoppingRoomId;


    @ApiModelProperty(name = "옷 이미지 url", example = "https://xxx.jpg")
    @Pattern(regexp = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)",
            message = "url 형식만 가능합니다.")
    private String imageUrl;
    
}
