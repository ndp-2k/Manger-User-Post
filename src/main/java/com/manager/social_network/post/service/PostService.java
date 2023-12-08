package com.manager.social_network.post.service;

import com.manager.social_network.common.constan.Message;
import com.manager.social_network.post.dto.PostRequest;
import com.manager.social_network.post.entity.Post;
import com.manager.social_network.post.mapper.PostRequestMapper;
import com.manager.social_network.post.repository.PostRepository;
import com.manager.social_network.user.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Transactional
@AllArgsConstructor
@Service
public class PostService {
    PostRepository postRepository;
    UserRepository userRepository;
    PostRequestMapper postRequestMapper;

    public Long createPost(Long userId, String content) {
        Post post = new Post();
        post.setContent(content);
        post.setCreateAt(Instant.now());
        post.setUserId(userId);
        post.setDeleteFlag(0);
        return postRepository.save(post).getId();
    }

    public void updatePost(Long id, PostRequest postRequest) {
        Post post = postRepository.getReferenceById(id);
        post.setContent(postRequest.getContent());
        postRepository.save(post);
    }

    public void deletePost(Long id) {
        Post post = postRepository.getReferenceById(id);
        post.setDeleteFlag(1);
        postRepository.save(post);
    }

    public boolean valid(Long id, Long userId) {
        return postRepository.findByIdAndUserId(id, userId).isPresent();
    }

    public boolean postIsExits(Long id) {
        return postRepository.existsById(id);
    }

    public Post getPostById(Long id) {
        return postRepository.getReferenceById(id);
    }

    public Page<Post> getNewFeed(Long userId, Pageable pageable) {
        return postRepository.getNewFeed(userId, Message.LAST_WEEK, pageable);
    }
}
