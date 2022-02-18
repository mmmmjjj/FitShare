package com.fitshare.backend.db.repository;

import com.fitshare.backend.db.entity.RoomParticipant;
import com.fitshare.backend.db.entity.ShoppingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {
    long countByShoppingRoom(ShoppingRoom shoppingRoom);

    Optional<RoomParticipant> findByMemberIdAndShoppingRoomId(@Param("memberId") Long memberId, @Param("shoppingRoomId") Long shoppingRoomId);

}
