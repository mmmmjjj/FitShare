package com.fitshare.backend.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shopping_room")
public class ShoppingRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    private Member host;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_mall_id")
    private ShoppingMall shoppingMall;

    @Column
    private String shoppingMallName;

    @Column
    private String shoppingMallUrl;

    @Column
    private int participantCount;

    @Column
    private String password;

    @Column
    private Boolean isPrivate;

    @Column
    private Boolean isActive;

    @Column
    private Boolean isCustomShoppingMall;

    @Column
    private String sessionId;

    @JsonIgnore
    @OneToMany(mappedBy = "shoppingRoom")
    List<RoomParticipant> roomParticipants = new ArrayList<>();
}
