package com.fitshare.backend.db.repository;

import com.fitshare.backend.db.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<Friend, Long> {
}
