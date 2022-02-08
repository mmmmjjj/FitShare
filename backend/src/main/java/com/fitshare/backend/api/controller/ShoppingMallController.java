package com.fitshare.backend.api.controller;

import com.fitshare.backend.api.service.ShoppingMallService;
import com.fitshare.backend.api.service.ShoppingRoomService;
import com.fitshare.backend.common.model.BaseResponseBody;
import com.fitshare.backend.db.entity.ShoppingMall;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.fitshare.backend.common.model.ResponseMessage.SEARCH_SHOPPING_MALL;

@Api(value = "쇼핑몰 API", tags = {"Shopping Mall"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shopping-malls")
public class ShoppingMallController {

    private final ShoppingMallService shoppingMallService;

    @ApiOperation(value = "쇼핑몰 검색", notes = "keyword 에 입력된 값이 포함된 쇼핑몰 이름 또는 쇼핑몰 url 을 검색하여 쇼핑몰 목록을 반환한다.")
    @GetMapping("")
    public ResponseEntity<BaseResponseBody> searchShoppingMalls(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK, SEARCH_SHOPPING_MALL, shoppingMallService.searchShoppingMalls(keyword)));
    }
}
