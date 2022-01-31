package com.fitshare.backend.db.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shopping_mall")
public class ShoppingMall extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String url;

    @Column
    private String logo;

    @OneToOne(mappedBy = "shoppingMall", fetch = FetchType.LAZY)
    private ShoppingRoom shoppingRoom;
}
