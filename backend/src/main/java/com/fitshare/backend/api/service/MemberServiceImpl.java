package com.fitshare.backend.api.service;

import com.fitshare.backend.api.response.BaseMemberRes;
import com.fitshare.backend.common.exception.EmailDuplicatedException;
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

        checkDuplicatedEmail(kakaoProfile.getKakao_account().getEmail());

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
        
        checkDuplicatedEmail(naverProfile.getResponse().getEmail());

        Member member = new Member();

        //네이버에서 받은 정보들로 회원 생성
        member.setUid(naverProfile.getResponse().getId());
        member.setName(naverProfile.getResponse().getName());
        member.setEmail(naverProfile.getResponse().getEmail());
        member.setProfileImg(naverProfile.getResponse().getProfile_image());
        member.setPhone(naverProfile.getResponse().getMobile());

        return createMember(member);
    }

    @Override
    public void updateProfileImage(Member member, String imageUrl) {
        member.setProfileImg(imageUrl);
        memberRepository.save(member);
    }

    @Override
    public Optional<Member> findMemberByUid(String uid) {
      
        return memberRepository.findByUid(uid);
    }

    @Override
    public Optional<Member> findMemberById(Long id) {

        return memberRepository.findById(id);
    }

    @Override
    public BaseMemberRes findMemberByEmail(String email) {
        return memberRepository.findActiveMemberByEmail(email);
    }

    private void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isPresent())
            throw new EmailDuplicatedException();
    }

}
