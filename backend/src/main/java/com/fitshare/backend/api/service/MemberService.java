package com.fitshare.backend.api.service;

import com.fitshare.backend.api.response.BaseMemberRes;
import com.fitshare.backend.common.model.KakaoProfile;
import com.fitshare.backend.common.model.NaverProfile;
import com.fitshare.backend.db.entity.Member;

import java.util.Optional;


public interface MemberService {

    Member createMember(Member member);

    Optional<Member> findMemberByUid(String uid);

    Optional<Member> findMemberById(Long id);

    BaseMemberRes findMemberByEmail(String email);

    Member createKakaoMember(KakaoProfile kakaoProfile);

    Member createNaverMember(NaverProfile naverProfile);

    void updateProfileImage(Member member,String imageUrl);
}
