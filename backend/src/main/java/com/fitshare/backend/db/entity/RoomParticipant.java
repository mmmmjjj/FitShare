package com.fitshare.backend.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "room_participant")
public class RoomParticipant extends CreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_room_id")
    private ShoppingRoom shoppingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public RoomParticipant(ShoppingRoom shoppingRoom, Member member) {
        this.shoppingRoom = shoppingRoom;
        this.member = member;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "roomParticipant")
    List<Cloth> clothes = new ArrayList<>();
}
