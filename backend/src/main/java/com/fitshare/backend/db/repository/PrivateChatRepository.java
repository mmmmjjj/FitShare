package com.fitshare.backend.db.repository;

import com.fitshare.backend.db.entity.PrivateChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateChatRepository extends JpaRepository<PrivateChat, Long> {
}
