package com.fitshare.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@ApiModel("MakeShoppingRoomRequest")
@Getter
@Setter
public class MakeShoppingRoomReq {

    @ApiModelProperty(name = "쇼핑몰 Id", example = "1")
    private Long shoppingMallId;

    @ApiModelProperty(name = "쇼핑몰 이름", example = "쇼핑몰이름")
    private String shoppingMallName;

    @ApiModelProperty(name = "쇼핑몰 url", example = "www.xxx.com")
    @Pattern(regexp = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)",
            message = "url 형식만 가능합니다.")
    private String shoppingMallUrl;

    @ApiModelProperty(name = "인원수", example = "2")
    @Min(value = 1, message = "인원 수는 1 이상이어야 합니다.")
    @Max(value = 6, message = "인원 수는 6 이하여야 합니다.")
    private int participantCount;

    @ApiModelProperty(name = "비밀번호", example = "1234")
    private String password;

    @ApiModelProperty(name = "공개여부", example = "false")
    private boolean isPrivate;

    @ApiModelProperty(name = "쇼핑몰 직접 입력", example = "false")
    private boolean isCustomShoppingMall;
}
