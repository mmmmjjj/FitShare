package com.fitshare.backend.db.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cloth")
public class Cloth extends CreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_participant_id")
    private RoomParticipant roomParticipant;

    @Column
    private String shoppingMallUrl;

    @Column
    private String clothUrl;
}
