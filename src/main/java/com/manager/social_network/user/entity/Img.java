package com.manager.social_network.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "img")
public class Img {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Long userId;
    @Column(name = "img")
    private String imgName;
    @Column
    private String type;
    @Column(name = "create_at")
    private Instant createAt;
}
