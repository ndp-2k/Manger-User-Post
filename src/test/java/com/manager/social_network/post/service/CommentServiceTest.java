package com.manager.social_network.post.service;

import com.manager.social_network.post.dto.CommentRequest;
import com.manager.social_network.post.entity.Comment;
import com.manager.social_network.post.mapper.CommentRequestMapper;
import com.manager.social_network.post.repository.CommentRepository;
import com.manager.social_network.user.respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRequestMapper commentRequestMapper;

    @Mock
    private PostService postService;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateComment() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent("Comment content");
        Mockito.when(commentRequestMapper.dtoToEntity(commentRequest)).thenReturn(new Comment());

        commentService.createComment(1L, commentRequest);

        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any(Comment.class));
    }

    @Test
    void testUpdateComment() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent("Updated comment content");

        Mockito.when(commentRepository.getReferenceById(1L)).thenReturn(new Comment());

        commentService.updateComment(1L, commentRequest);

        Mockito.verify(commentRepository, Mockito.times(1)).
                save(Mockito.any(Comment.class));
    }

    @Test
    void whenCommentExists() {
        Comment comment = new Comment();
        comment.setId(2L);
        comment.setPostId(1L);
        Mockito.when(commentRepository.existsById(2L)).thenReturn(true);
        Mockito.when(commentRepository.getReferenceById(2L)).thenReturn(comment);
        Mockito.when(postService.postIsExits(1L)).thenReturn(true);

        assertTrue(commentService.commentExits(2L));
    }

    @Test
    void whenCommentDoesNotExist() {
        Mockito.when(commentRepository.existsById(1L)).thenReturn(false);

        assertFalse(commentService.commentExits(1L));
    }

    @Test
    void whenIsValidOk() {
        Mockito.when(commentRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(new Comment()));

        assertTrue(commentService.isValid(1L, 1L));
    }

    @Test
    void whenIsValidFail() {
        Mockito.when(commentRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertFalse(commentService.isValid(1L, 1L));
    }

    @Test
    void whenGetCommentById() {
        Comment comment = new Comment();
        comment.setContent("Content");
        Mockito.when(commentRepository.getReferenceById(1L)).thenReturn(comment);

        assertEquals(comment.getContent(), commentService.getCommentById(1L).getContent());
    }
}