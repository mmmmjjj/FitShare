package com.fitshare.backend.db.repository;

import com.fitshare.backend.api.response.ClothRes;
import com.fitshare.backend.db.entity.Cloth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClothRepository extends JpaRepository<Cloth, Long> {

    @Query(value = "select new com.fitshare.backend.api.response.ClothRes" +
            "( c.id, c.clothUrl ) " +
            "from Cloth c " +
            "join c.roomParticipant r " +
            "where r.member.id = :memberId " +
            "and r.shoppingRoom.id = :shoppingRoomId " +
            "order by c.createdTime desc")
    Optional<List<ClothRes>> getClothByMemberId(@Param("memberId") Long memberId,@Param("shoppingRoomId") Long shoppingRoomId);

}
