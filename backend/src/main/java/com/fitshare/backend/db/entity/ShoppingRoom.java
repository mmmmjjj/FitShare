package com.fitshare.backend.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
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
    private int password;

    @Column
    private Boolean isPrivate;

    @Column
    private Boolean isActive;

    @Column
    private Boolean isCustomShoppingMall;

    @OneToOne(mappedBy = "shoppingRoom", fetch = FetchType.LAZY)
    private GroupChat groupChat;
}
