package com.manager.social_network.post.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.manager.social_network.post.entity.Post}
 */
@Getter
@Setter
public class PostRequest {
    String content;
}