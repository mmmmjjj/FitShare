package com.fitshare.backend.db.repository;

import com.fitshare.backend.api.response.PrivateChatRes;
import com.fitshare.backend.db.entity.PrivateChat;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateChatRepository extends JpaRepository<PrivateChat, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE PrivateChat p" +
            "   SET p.isChecked = true" +
            " WHERE p.sender.id = :senderId" +
            "   AND p.receiver.id = :receiverId" +
            "   AND p.isChecked = false")
    void updateIsChecked(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    @Query(value = "SELECT NEW com.fitshare.backend.api.response.PrivateChatRes" +
            "( p.sender.id, p.receiver.id, p.message, p.isChecked, p.createdTime )" +
            "  FROM PrivateChat p" +
            " WHERE SUBSTRING(p.createdTime, 1, 10) = :date" +
            "   AND ((p.sender.id = :senderId AND p.receiver.id = :receiverId)" +
            "         OR (p.sender.id = :receiverId AND p.receiver.id = :senderId))" +
            " ORDER BY p.createdTime DESC ")
    List<PrivateChatRes> findByCreatedTime(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId, @Param("date") String date);

}
