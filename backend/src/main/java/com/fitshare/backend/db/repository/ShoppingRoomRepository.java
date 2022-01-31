package com.fitshare.backend.db.repository;

import com.fitshare.backend.db.entity.ShoppingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingRoomRepository extends JpaRepository<ShoppingRoom, Long> {
}
