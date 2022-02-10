package com.fitshare.backend.api.service;

import com.fitshare.backend.api.response.BaseMemberRes;
import com.fitshare.backend.common.model.KakaoProfile;
import com.fitshare.backend.common.model.NaverProfile;
import com.fitshare.backend.db.entity.Member;
import com.fitshare.backend.db.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService  {

    private final MemberRepository memberRepository;

    @Override
    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member createKakaoMember(KakaoProfile kakaoProfile) {
        Member member = new Member();

        //카카오에서 받은 정보들로 회원 생성
        member.setUid(Long.toString(kakaoProfile.getId()));
        member.setName(kakaoProfile.getKakao_account().getProfile().getNickname());
        member.setEmail(kakaoProfile.getKakao_account().getEmail());
        member.setProfileImg(kakaoProfile.getKakao_account().getProfile().getProfile_image_url());

        return createMember(member);
    }

    @Override
    public Member createNaverMember(NaverProfile naverProfile) {
        Member member = new Member();

        //네이버에서 받은 정보들로 회원 생성
        member.setUid(naverProfile.getResponse().getId());
        member.setName(naverProfile.getResponse().getName());
        member.setEmail(naverProfile.getResponse().getEmail());
        member.setProfileImg(naverProfile.getResponse().getMobile());
        member.setPhone(naverProfile.getResponse().getMobile());

        return createMember(member);
    }

    @Override
    public Optional<Member> findMemberByUid(String uid) {

        Optional<Member> member = memberRepository.findByUid(uid);

        if(member.isPresent())
            return member;

        return null;
    }

    @Override
    public Optional<Member> findMemberById(Long id) {

        Optional<Member> member = memberRepository.findById(id);

        if (member.isPresent()) return member;

        return null;
    }

    @Override
    public BaseMemberRes findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
