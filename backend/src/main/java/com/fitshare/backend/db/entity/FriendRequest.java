package com.fitshare.backend.db.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "friend_request")
public class FriendRequest extends CreatedTimeEntity {

    public FriendRequest(Member member, Member targetMember) {
        this.member = member;
        this.targetMember = targetMember;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_member_id")
    private Member targetMember;
}
