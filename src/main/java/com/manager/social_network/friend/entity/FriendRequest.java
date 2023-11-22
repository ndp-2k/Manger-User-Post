package com.manager.social_network.friend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@Entity
@Table(name = "friend_request")
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "from_user")
    private Long from;
    @Column(name = "to_user")
    private Long to;
    @Column(name = "create_at")
    private Instant createAt;
}
