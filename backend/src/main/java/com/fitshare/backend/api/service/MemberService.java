package com.fitshare.backend.api.service;

import com.fitshare.backend.common.model.KakaoProfile;
import com.fitshare.backend.db.entity.Member;

import java.util.Optional;


public interface MemberService {

    Member createMember(KakaoProfile kakaoProfile);

    Optional<Member> findMemberByUid(Long uid);

    Optional<Member> findMemberById(Long id);
}
