package com.manager.social_network.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.social_network.common.constan.Message;
import com.manager.social_network.common.function.Common;
import com.manager.social_network.friend.service.FriendService;
import com.manager.social_network.post.dto.PostRequest;
import com.manager.social_network.post.service.CommentService;
import com.manager.social_network.post.service.LikeService;
import com.manager.social_network.post.service.PostService;
import com.manager.social_network.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest {

    PostRequest postRequest;
    ObjectMapper objectMapper;
    private MockMvc mockMvc;
    @Mock
    private Common common;
    @Mock
    private PostService postService;
    @Mock
    private UserService userService;
    @Mock
    private CommentService commentService;
    @Mock
    private FriendService friendService;
    @Mock
    private LikeService likeService;
    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setup() {
        postRequest = new PostRequest();
        postRequest.setContent("Test content");

        objectMapper = new ObjectMapper();

        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    void testCreatePost() throws Exception {
        when(common.getUserIdByToken(any())).thenReturn(12L);
        doNothing().when(postService).createPost(12L, postRequest);

        mockMvc.perform(post("/api/v1/posts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Message.SUCCESS));
    }

    @Test
    void testEditPost() throws Exception {
        Long postId = 1L;
        when(common.getUserIdByToken(any())).thenReturn(27L);
        when(postService.valid(postId, 27L)).thenReturn(true);
        doNothing().when(postService).updatePost(anyLong(), any(PostRequest.class));

        mockMvc.perform(patch("/api/v1/posts/edit/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Message.SUCCESS));

        verify(postService, times(1)).updatePost(eq(1L), any(PostRequest.class));
    }

    @Test
    void testEditPostNotFound() throws Exception {
        Long postId = 1L;
        when(common.getUserIdByToken(any())).thenReturn(27L);
        when(postService.valid(postId, 27L)).thenReturn(false);

        mockMvc.perform(patch("/api/v1/posts/edit/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Message.NOT_FOUND_POST));
    }

}
