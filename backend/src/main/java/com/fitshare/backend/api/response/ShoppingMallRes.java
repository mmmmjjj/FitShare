package com.fitshare.backend.api.response;

import com.fitshare.backend.db.entity.ShoppingMall;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@ApiModel("ShoppingMallResponse")
@Getter
@Setter
public class ShoppingMallRes {

    @ApiModelProperty(name = "쇼핑몰 Id", example = "1")
    private Long id;

    @ApiModelProperty(name = "쇼핑몰 이름", example = "쇼핑몰 이름")
    private String name;

    @ApiModelProperty(name = "쇼핑몰 url", example = "www.xxx.com")
    private String url;

    @ApiModelProperty(name = "쇼핑몰 logo", example = "쇼핑몰 로고 이미지 경로")
    private String logo;

    public ShoppingMallRes(ShoppingMall shoppingMall) {
        this.id = shoppingMall.getId();
        this.name = shoppingMall.getName();
        this.url = shoppingMall.getUrl();
        this.logo = shoppingMall.getLogo();
    }
}
