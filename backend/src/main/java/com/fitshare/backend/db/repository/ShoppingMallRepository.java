package com.fitshare.backend.db.repository;

import com.fitshare.backend.db.entity.ShoppingMall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingMallRepository extends JpaRepository<ShoppingMall, Long> {
}
