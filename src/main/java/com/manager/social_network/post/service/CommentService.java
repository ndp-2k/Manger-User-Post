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

@Transactional
@AllArgsConstructor
@Service
public class CommentService {
    CommentRepository commentRepository;
    UserRepository userRepository;
    CommentRequestMapper commentRequestMapper;
    PostService postService;

    public void createComment(Long userid, CommentRequest commentRequest) {
        Comment comment = commentRequestMapper.dtoToEntity(commentRequest);
        comment.setUserId(userid);
        comment.setCreateAt(Instant.now());
        comment.setDeleteFlag(0);
        commentRepository.save(comment);
    }

    public void updateComment(Long id, CommentRequest commentRequest) {
        Comment comment = commentRepository.getReferenceById(id);
        comment.setContent(commentRequest.getContent());
        comment.setImgId(comment.getImgId());
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
}
