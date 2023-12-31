package com.manager.social_network.friend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "user")
    private Long user;
    @Column(name = "user_second")
    private Long userSecond;
    @Column(name = "friend_at")
    private Instant friendAt;
}
