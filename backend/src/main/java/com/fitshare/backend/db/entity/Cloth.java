package com.fitshare.backend.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "cloth")
public class Cloth extends CreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closet_id")
    private Closet closet;

    @Column
    private String shoppingMallUrl;

    @Column
    private String clothUrl;
}
