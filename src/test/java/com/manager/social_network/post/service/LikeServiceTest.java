package com.manager.social_network.post.service;

import com.manager.social_network.post.entity.Like;
import com.manager.social_network.post.repository.LikeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class LikeServiceTest {

    @InjectMocks
    LikeService likeService;

    @Mock
    LikeRepository likeRepository;


    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void whenLike() {
        Long userId = 27L;
        Long postId = 1L;
        Mockito.when(likeRepository.findByUserIdAndTheIdAndType(userId, postId, "post")).thenReturn(Optional.empty());

        int result = likeService.likePost(userId, postId);
        verify(likeRepository, times(1)).save(any());
        Assertions.assertEquals(1, result);
    }

    @Test
    void whenUnLike() {
        Long userId = 27L;
        Long postId = 1L;
        Like like =new
                Like();
        Mockito.when(likeRepository.findByUserIdAndTheIdAndType(userId, postId, "post")).thenReturn(Optional.of(like));

        int result = likeService.likePost(userId, postId);
        verify(likeRepository, times(1)).delete(like);
        Assertions.assertEquals(0, result);
    }
}