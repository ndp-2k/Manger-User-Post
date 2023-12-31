package com.manager.social_network.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "content")
    private String content;
    @JsonIgnore
    @Column(name = "delete_flag")
    private Integer deleteFlag;
    @Column(name = "create_at")
    private Instant createAt;
    @Column(name = "update_at")
    private Instant updateAt;

}
