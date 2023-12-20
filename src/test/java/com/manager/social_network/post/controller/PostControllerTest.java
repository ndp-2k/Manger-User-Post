package com.manager.social_network.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.social_network.common.constan.Message;
import com.manager.social_network.common.function.Common;
import com.manager.social_network.friend.service.FriendService;
import com.manager.social_network.post.dto.CommentRequest;
import com.manager.social_network.post.dto.PostRequest;
import com.manager.social_network.post.entity.Comment;
import com.manager.social_network.post.entity.Post;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest {

    PostRequest postRequest;
    ObjectMapper objectMapper;
    CommentRequest commentRequest;
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

    // Utility method to convert object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setup() {
        commentRequest = new CommentRequest();
        commentRequest.setContent("content");
        postRequest = new PostRequest();
        postRequest.setContent("Test content");

        objectMapper = new ObjectMapper();

        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

//    @Test
//    void testCreatePost() throws Exception {
//        when(common.getUserIdByToken(any())).thenReturn(12L);
//        doNothing().when(postService).createPost(12L, "content");
//
//        mockMvc.perform(post("/api/v1/posts/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString("content")))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(Message.SUCCESS));
//    }

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

    @Test
    void testDeletePost() throws Exception {
        Long postId = 1L;
        when(common.getUserIdByToken(any())).thenReturn(27L);
        when(postService.valid(postId, 27L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/posts/delete/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Message.SUCCESS));
    }

    @Test
    void testDeletePostNotFound() throws Exception {
        Long postId = 1L;

        when(common.getUserIdByToken(any())).thenReturn(27L);
        when(postService.valid(postId, 27L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/posts/delete/{id}", postId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Message.NOT_FOUND_POST));
    }

    @Test
    void testGetPost() throws Exception {
        Long postId = 1L;
        Post mockPost = new Post();

        when(common.getUserIdByToken(any())).thenReturn(27L);
        when(postService.postIsExits(postId)).thenReturn(true);
        when(friendService.isFriend(mockPost.getUserId(), 27L)).thenReturn(true);
        when(postService.getPostById(postId)).thenReturn(mockPost);

        mockMvc.perform(get("/api/v1/posts/get/{id}", postId))
                .andExpect(status().isOk());
    }


    @Test
    void testGetPostNotFound() throws Exception {
        Long postId = 1L;

        when(common.getUserIdByToken(any())).thenReturn(27L);
        when(postService.postIsExits(postId)).thenReturn(false);

        mockMvc.perform(get("/api/v1/posts/get/{id}", postId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPostNotFoundCaseNotFriend() throws Exception {
        Long postId = 1L;
        Post mockPost = new Post();
        mockPost.setUserId(13L);

        when(common.getUserIdByToken(any())).thenReturn(27L);
        when(postService.getPostById(postId)).thenReturn(mockPost);
        when(postService.postIsExits(postId)).thenReturn(true);
        when(friendService.isFriend(mockPost.getUserId(), 27L)).thenReturn(false);

        mockMvc.perform(get("/api/v1/posts/get/{id}", postId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(Message.NOT_ALREADY_FRIEND));
    }

    @Test
    void testCreateComment() throws Exception {
        Long postId = 1L;
        Post post = new Post();
        post.setUserId(12L);

        when(postService.postIsExits(postId)).thenReturn(true);
        when(postService.getPostById(postId)).thenReturn(post);
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(friendService.isFriend(12L, 12L)).thenReturn(true);
        doNothing().when(commentService).createComment(12L, "content",postId);
        mockMvc.perform(post("/api/v1/posts/comment/create/{post_id}", postId)
                        .content("content")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Message.SUCCESS));
    }

    @Test
    void testCreateCommentNotFoundPost() throws Exception {
        Long postId = 1L;

        when(postService.postIsExits(postId)).thenReturn(false);

        mockMvc.perform(post("/api/v1/posts/comment/create/{post_id}", postId)
                        .content(asJsonString(commentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Message.NOT_FOUND_POST));
    }

    @Test
    void testCreateCommentNotFriend() throws Exception {
        Long postId = 1L;

        Post post = new Post();
        post.setUserId(12L);

        when(postService.postIsExits(postId)).thenReturn(true);
        when(postService.getPostById(postId)).thenReturn(post);
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(friendService.isFriend(12L, 12L)).thenReturn(false);

        mockMvc.perform(post("/api/v1/posts/comment/create/{post_id}", postId)
                        .content(asJsonString(commentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Message.NOT_ALREADY_FRIEND));
    }

    @Test
    void testEditComment() throws Exception {
        Long commentId = 1L;

        when(commentService.commentExits(commentId)).thenReturn(true);
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(commentService.isValid(commentId, 12L)).thenReturn(true);
        doNothing().when(commentService).updateComment(commentId, commentRequest);

        mockMvc.perform(patch("/api/v1/posts/comment/edit/{id}", commentId)
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Message.SUCCESS));
    }

    @Test
    void testEditCommentNotFound() throws Exception {
        Long commentId = 1L;

        when(commentService.commentExits(commentId)).thenReturn(false);

        mockMvc.perform(patch("/api/v1/posts/comment/edit/{id}", commentId)
                        .content(asJsonString(commentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Message.NOT_FOUND_COMMENT));
    }

    @Test
    void testEditCommentNotValid() throws Exception {
        Long commentId = 1L;

        when(commentService.commentExits(commentId)).thenReturn(true);
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(commentService.isValid(commentId, 12L)).thenReturn(false);

        mockMvc.perform(patch("/api/v1/posts/comment/edit/{id}", commentId)
                        .content(asJsonString(commentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value(Message.NOT_ALLOWED));
    }

    @Test
    void testGetComment() throws Exception {
        Long commentId = 1L;
        Comment comment = new
                Comment();
        comment.setUserId(12L);

        when(commentService.commentExits(commentId)).thenReturn(true);
        when(commentService.getCommentById(commentId)).thenReturn(comment);
        when(common.getUserIdByToken(any())).thenReturn(27L);
        when(friendService.isFriend(comment.getUserId(), 27L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/posts/comment/get/{id}", commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isNotEmpty());
    }

    @Test
    void testGetCommentNotFriend() throws Exception {
        Long commentId = 1L;
        Comment comment = new
                Comment();
        comment.setUserId(12L);

        when(commentService.commentExits(commentId)).thenReturn(true);
        when(commentService.getCommentById(commentId)).thenReturn(comment);
        when(common.getUserIdByToken(any())).thenReturn(27L);
        when(friendService.isFriend(comment.getUserId(), 27L)).thenReturn(false);

        mockMvc.perform(get("/api/v1/posts/comment/get/{id}", commentId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(Message.NOT_ALREADY_FRIEND));
    }

    @Test
    void testGetCommentNotFound() throws Exception {
        Long commentId = 1L;

        when(commentService.commentExits(commentId)).thenReturn(false);

        mockMvc.perform(get("/api/v1/posts/comment/get/{id}", commentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(Message.NOT_FOUND_COMMENT));
    }

    @Test
    void testUnLikePost() throws Exception {
        Long postId = 1L;

        Post post = new Post();
        post.setUserId(12L);

        when(postService.postIsExits(postId)).thenReturn(true);
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(postService.getPostById(postId)).thenReturn(post);
        when(friendService.isFriend(12L, 27L)).thenReturn(true);
        when(likeService.likePost(12L, 27L)).thenReturn(0);

        mockMvc.perform(get("/api/v1/posts/like/{post_id}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Message.UN_LIKE_POST));
    }

    @Test
    void testLikePost() throws Exception {
        Long postId = 1L;

        Post post = new Post();
        post.setUserId(27L);

        when(postService.postIsExits(postId)).thenReturn(true);
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(postService.getPostById(postId)).thenReturn(post);
        when(friendService.isFriend(12L, 27L)).thenReturn(true);
        when(likeService.likePost(12L, 1L)).thenReturn(1);

        mockMvc.perform(get("/api/v1/posts/like/{post_id}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Message.LIKE_POST));
    }

    @Test
    void testLikePostError() throws Exception {
        Long postId = 1L;

        Post post = new Post();
        post.setUserId(27L);

        when(postService.postIsExits(postId)).thenReturn(true);
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(postService.getPostById(postId)).thenReturn(post);
        when(friendService.isFriend(12L, 1L)).thenReturn(false);

        mockMvc.perform(get("/api/v1/posts/like/{post_id}", postId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Message.NOT_ALREADY_FRIEND));
    }

    @Test
    void testLikePostNotFound() throws Exception {
        Long postId = 1L;

        when(postService.postIsExits(postId)).thenReturn(false);

        mockMvc.perform(get("/api/v1/posts/like/{post_id}", postId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Message.NOT_FOUND_POST));
    }
}
