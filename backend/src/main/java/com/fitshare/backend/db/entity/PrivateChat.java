package com.fitshare.backend.db.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "private_chat")
public class PrivateChat {

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

        @Column
        @CreatedDate
        private LocalDateTime createdTime;

        @Builder
        public PrivateChat(Member sender, Member receiver, String message, LocalDateTime createdTime) {
                this.sender = sender;
                this.receiver = receiver;
                this.message = message;
                this.isChecked = false;
                this.createdTime = createdTime;
        }

}
