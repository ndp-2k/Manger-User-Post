package com.manager.social_network.post.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "post_id")
    private Long postId;
    @Column(name = "full_name")
    private String content;
    @Column(name = "img_id")
    private Long imgId;

    @Column(name = "delete_flag")
    private Integer deleteFlag;
    @Column(name = "create_at")
    private Instant createAt;

}