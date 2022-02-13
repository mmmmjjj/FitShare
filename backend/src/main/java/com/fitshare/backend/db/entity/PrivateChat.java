package com.fitshare.backend.db.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "private_chat")
public class PrivateChat extends CreatedTimeEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "sender_id")
        private Member sender;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "receiver_id")
        private Member receiver;

        @Column
        private String message;

        @Column
        private Boolean isChecked;

        @Builder
        public PrivateChat(Member sender, Member receiver, String message) {
                this.sender = sender;
                this.receiver = receiver;
                this.message = message;
                this.isChecked = false;
        }
}
