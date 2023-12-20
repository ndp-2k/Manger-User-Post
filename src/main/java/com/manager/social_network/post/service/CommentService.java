package com.manager.social_network.post.service;

import com.manager.social_network.post.dto.CommentRequest;
import com.manager.social_network.post.entity.Comment;
import com.manager.social_network.post.mapper.CommentRequestMapper;
import com.manager.social_network.post.repository.CommentRepository;
import com.manager.social_network.user.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Transactional
@AllArgsConstructor
@Service
public class CommentService {
    CommentRepository commentRepository;
    UserRepository userRepository;
    CommentRequestMapper commentRequestMapper;
    PostService postService;

    public void createComment(Long userid, String content, Long postId) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUserId(userid);
        comment.setPostId(postId);
        comment.setCreateAt(Instant.now());
        comment.setDeleteFlag(0);
        commentRepository.save(comment);
    }

    public void updateComment(Long id, CommentRequest commentRequest) {
        Comment comment = commentRepository.getReferenceById(id);
        comment.setContent(commentRequest.getContent());
        comment.setUpdateAt(Instant.now());
        commentRepository.save(comment);
    }

    public boolean commentExits(Long id) {
        return commentRepository.existsById(id) &&
                postService.postIsExits(commentRepository.getReferenceById(id).getPostId());
    }


    public boolean isValid(Long id, Long userId) {
        return commentRepository.findByIdAndUserId(id, userId).isPresent();
    }

    public Comment getCommentById(Long id) {
        return commentRepository.getReferenceById(id);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }
}
