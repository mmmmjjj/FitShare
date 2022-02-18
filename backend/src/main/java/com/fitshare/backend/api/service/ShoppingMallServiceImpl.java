package com.fitshare.backend.api.service;

import com.fitshare.backend.api.response.ShoppingMallRes;
import com.fitshare.backend.db.entity.ShoppingMall;
import com.fitshare.backend.db.repository.ShoppingMallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShoppingMallServiceImpl implements ShoppingMallService {

    private final ShoppingMallRepository shoppingMallRepository;

    @Override
    public List<ShoppingMallRes> searchShoppingMalls(String keyword) {

        return shoppingMallRepository.findShoppingMallsByNameContainingOrUrlContaining(keyword)
                .stream().map(ShoppingMallRes::new)
                .collect(Collectors.toList());
    }
}
