package com.fitshare.backend.db.repository;

import com.fitshare.backend.db.entity.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {
}
