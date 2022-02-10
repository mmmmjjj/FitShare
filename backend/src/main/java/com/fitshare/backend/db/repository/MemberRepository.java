package com.fitshare.backend.db.repository;

import com.fitshare.backend.api.response.BaseMemberRes;
import com.fitshare.backend.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUid(String uid);

    Optional<Member> findByEmail(String email);

    @Query(value = "SELECT NEW com.fitshare.backend.api.response.BaseMemberRes" +
            "( m.id, m.name, m.profileImg, m.email )" +
            "  FROM Member m" +
            " WHERE m.isActive = true" +
            "   AND m.email = :email" +
            " ORDER BY m.name")
    BaseMemberRes findActiveMemberByEmail(@Param("email") String email);
}
