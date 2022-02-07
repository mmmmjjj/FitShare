package com.fitshare.backend.db.repository;

import com.fitshare.backend.api.response.ShoppingRoomRes;
import com.fitshare.backend.db.entity.ShoppingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingRoomRepository extends JpaRepository<ShoppingRoom, Long> {

    @Query("select new com.fitshare.backend.api.response.ShoppingRoomRes(" +
            "m.name, s.id, s.shoppingMallName, s.shoppingMallUrl, s.isPrivate, s.participantCount, count(r.id)) " +
            "from ShoppingRoom s " +
            "join s.roomParticipants r " +
            "join Friend f " +
            "on s.host = f.friend " +
            "join f.friend m " +
            "where s.isActive = true " +
            "and f.member.id = :memberId " +
            "group by s " +
            "order by s.createdTime desc ")
    Optional<List<ShoppingRoomRes>> findShoppingRoomsByMemberId(@Param("memberId") Long memberId);
}
