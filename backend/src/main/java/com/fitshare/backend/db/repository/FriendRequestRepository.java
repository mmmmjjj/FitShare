package com.fitshare.backend.db.repository;

import com.fitshare.backend.api.response.FriendRes;
import com.fitshare.backend.db.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    /**
     * friend_request table 에서 memberId 와 friendId 로 두 사용자에 관한 친구 요청이 이미 존재하는지 판단하기 위해 사용한다
     * return 값이 0이라면 기존의 요청이 없고, 1이라면 이미 요청이 존재한다고 판단한다
     */
    int countByMemberIdAndTargetMemberId(Long memberId, Long targetMemberId);

    /**
     * friend_request table 에서 targetMemberId 에게 친구 요청한 사용자들을 찾는다
     */
    @Query(value = "SELECT NEW com.fitshare.backend.api.response.FriendRes" +
            "( m.id, m.name, m.email, m.phone, m.profileImg, f.createdTime )" +
            "  FROM FriendRequest f" +
            "  JOIN f.member m" +
            " WHERE f.targetMember.id = :targetMemberId" +
            "   AND m.isActive = true" +
            " ORDER BY m.name")
    List<FriendRes> findByTargetMemberId(@Param("targetMemberId") Long targetMemberId);

    /**
     * friend_request table 에서 memberId 가 targetMemberId 에게 한 친구 요청을 삭제한다
     */
    int removeByMemberIdAndTargetMemberId(Long memberId, Long targetMemberId);

}
