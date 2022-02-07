package com.fitshare.backend.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@ApiModel("AddClothResponse")
@Getter
@Setter
@AllArgsConstructor
public class ClothRes {

    @ApiModelProperty(name = "옷 Id", example = "1")
    private Long clothId;

    @ApiModelProperty(name = "배경 제거 이미지 경로", example = "")
    private String imagePath;

}
