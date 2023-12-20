package com.manager.social_network.post.service;

import com.manager.social_network.post.entity.Like;
import com.manager.social_network.post.repository.LikeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor

public class LikeService {
    LikeRepository likeRepository;

    public int likePost(Long userId, Long postId) {
        Optional<Like> optionalLike = likeRepository.findByUserIdAndTheIdAndType(userId, postId, "post");
        if (optionalLike.isEmpty()) {
            Like like = new Like();
            like.setUserId(userId);
            like.setTheId(postId);
            like.setLikeAt(Instant.now());
            like.setType("post");
            likeRepository.save(like);
            return 1;
        } else {
            likeRepository.delete(optionalLike.get());
            return 0;
        }
    }

    public Long getSumLikePost(Long postId) {
        return likeRepository.countByIdWherePostId(postId);
    }

    public Boolean isLiked(Long postId, Long userIdByToken) {
        return likeRepository.findByUserIdAndTheId(userIdByToken,postId ).isPresent();
    }
}

