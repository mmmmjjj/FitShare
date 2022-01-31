package com.fitshare.backend.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
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
}
