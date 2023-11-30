package com.manager.social_network.post.service;


import com.manager.social_network.common.constan.Message;
import com.manager.social_network.post.dto.PostRequest;
import com.manager.social_network.post.entity.Post;
import com.manager.social_network.post.mapper.PostRequestMapper;
import com.manager.social_network.post.repository.PostRepository;
import com.manager.social_network.user.respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRequestMapper postRequestMapper;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreatePost() {
        PostRequest postRequest = new PostRequest();
        Post post = new Post();

        when(postRequestMapper.dtoToEntity(postRequest)).thenReturn(post);

        postService.createPost(27L, postRequest);

        verify(postRepository, times(1)).save(post);
        assertEquals(27L, post.getUserId());
    }

    @Test
    void testUpdatePost() {
        Long postId = 1L;
        PostRequest postRequest = new PostRequest();
        postRequest.setContent("post request content");
        Post post = new Post();
        post.setContent("post content");

        when(postRepository.getReferenceById(postId)).thenReturn(post);

        postService.updatePost(postId, postRequest);

        verify(postRepository, times(1)).save(post);
        assertEquals(post.getContent(), postRequest.getContent());
    }

    @Test
    void testDeletePost() {
        Long postId = 1L;
        Post post = new Post();

        when(postRepository.getReferenceById(postId)).thenReturn(post);

        postService.deletePost(postId);

        verify(postRepository, times(1)).save(post);
        assertEquals(1, post.getDeleteFlag());
    }

    @Test
    void testValid() {
        Long postId = 1L;
        Long userId = 2L;

        when(postRepository.findByIdAndUserId(postId, userId)).thenReturn(Optional.of(new Post()));

        assertTrue(postService.valid(postId, userId));
    }

    @Test
    void testInvalidValid() {
        Long postId = 1L;
        Long userId = 2L;

        when(postRepository.findByIdAndUserId(postId, userId)).thenReturn(Optional.empty());

        assertFalse(postService.valid(postId, userId));
    }

    @Test
    void testPostExists() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(true);

        assertTrue(postService.postIsExits(postId));
    }

    @Test
    void testPostNotExists() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(false);

        assertFalse(postService.postIsExits(postId));
    }

    @Test
    void testGetPostById() {
        Long postId = 1L;
        Post post = new Post();

        when(postRepository.getReferenceById(postId)).thenReturn(post);

        assertEquals(post, postService.getPostById(postId));
    }

    @Test
    void testGetNewFeed() {
        Long userId = 1L;
        List<Post> expectedPosts = Collections.singletonList(new Post());

        when(postRepository.getNewFeed(userId, Message.LAST_WEEK)).thenReturn(expectedPosts);

        assertEquals(expectedPosts, postService.getNewFeed(userId));
    }
}