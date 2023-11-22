package com.manager.social_network.post.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.manager.social_network.post.entity.Post}
 */
@Getter
@Setter
public class CommentRequest {
    @NotEmpty
    String content;
    Long imgId;
}