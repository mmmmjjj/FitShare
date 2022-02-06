package com.fitshare.backend.db.repository;

import com.fitshare.backend.api.response.FriendRes;
import com.fitshare.backend.db.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    /**
     * friend table 에서 memberId 와 friendId 로 두 사용자가 이미 친구인지 판단하기 위해 사용한다
     * return 값이 0이라면 친구가 아니고, 1이라면 친구라고 판단한다
     */
    int countByMemberIdAndFriendId(Long memberId, Long friendId);

    /**
     * friend table 에서 memberId 의 친구들을 찾는다
     */
    @Query(value = "SELECT NEW com.fitshare.backend.api.response.FriendRes" +
            "( f.friend.id, m.name, m.email, m.phone, m.profileImg, f.createdTime )" +
            "  FROM Friend f" +
            "  JOIN f.friend m" +
            " WHERE f.member.id = :memberId" +
            "   AND m.isActive = true" +
            " ORDER BY m.name")
    List<FriendRes> findByMemberId(@Param("memberId") Long memberId);

    /**
     * friend table 에서 memberId 의 친구들 중 email 이 friendEmail 을 포함하는 친구들을 찾는다
     */
    @Query(value = "SELECT NEW com.fitshare.backend.api.response.FriendRes" +
            "( f.friend.id, m.name, m.email, m.phone, m.profileImg, f.createdTime )" +
            "  FROM Friend f" +
            "  JOIN f.friend m" +
            " WHERE f.member.id = :memberId" +
            "   AND m.isActive = true" +
            "   AND f.friend.email LIKE %:friendEmail%" +
            " ORDER BY m.name")
    List<FriendRes> findByMemberIdAndFriendEmailLike(@Param("memberId") Long memberId, @Param("friendEmail") String friendEmail);

    /**
     * friend table 에서 memberId 의 친구인 friendId 를 삭제한다
     */
    int removeByMemberIdAndFriendId(Long memberId, Long friendId);

}
