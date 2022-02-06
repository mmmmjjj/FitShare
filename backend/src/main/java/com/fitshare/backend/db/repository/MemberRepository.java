package com.fitshare.backend.db.repository;

import com.fitshare.backend.api.response.BaseMemberRes;
import com.fitshare.backend.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUid(Long uid);

    @Query(value = "SELECT NEW com.fitshare.backend.api.response.BaseMemberRes" +
            "( m.id, m.name, m.profileImg, m.email )" +
            "  FROM Member m" +
            " WHERE m.email LIKE %:email%" +
            "   AND m.isActive = true" +
            " ORDER BY m.name")
    List<BaseMemberRes> findByEmailLike(@Param("email") String email);

}
