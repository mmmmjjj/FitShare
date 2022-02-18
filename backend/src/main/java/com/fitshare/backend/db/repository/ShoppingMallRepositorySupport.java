package com.fitshare.backend.db.repository;

import com.fitshare.backend.db.entity.ShoppingMall;

import java.util.List;

public interface ShoppingMallRepositorySupport {
    List<ShoppingMall> findShoppingMallsByNameContainingOrUrlContaining(String keyword);
}
