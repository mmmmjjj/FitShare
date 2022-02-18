package com.fitshare.backend.api.service;

import com.fitshare.backend.api.response.ShoppingMallRes;

import java.util.List;

public interface ShoppingMallService {

    List<ShoppingMallRes> searchShoppingMalls(String keyword);
}
