package com.fitshare.backend.db.repository;

import com.fitshare.backend.db.entity.Closet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClosetRepository extends JpaRepository<Closet, Long> {
}
