package com.fitshare.backend.db.repository;

import com.fitshare.backend.db.entity.ShoppingMall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingMallRepository extends JpaRepository<ShoppingMall, Long> {

    List<ShoppingMall> findShoppingMallsByNameContainingOrUrlContaining(String nameKeyword, String urlKeyword);
}
