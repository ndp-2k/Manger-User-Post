package com.manager.social_network.post.service;

import com.manager.social_network.common.constan.Message;
import com.manager.social_network.post.dto.PostRequest;
import com.manager.social_network.post.entity.Post;
import com.manager.social_network.post.mapper.PostRequestMapper;
import com.manager.social_network.post.repository.PostRepository;
import com.manager.social_network.user.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Transactional
@AllArgsConstructor
@Service
public class PostService {
    PostRepository postRepository;
    UserRepository userRepository;
    PostRequestMapper postRequestMapper;

    public void createPost(Long userId, PostRequest postRequest) {
        Post post = postRequestMapper.dtoToEntity(postRequest);
        post.setCreateAt(Instant.now());
        post.setUserId(userId);
        post.setDeleteFlag(0);
        postRepository.save(post);
    }

    public void updatePost(Long id, PostRequest postRequest) {
        Post post = postRepository.getReferenceById(id);
        post.setContent(postRequest.getContent());
        post.setImgId(postRequest.getImgId());
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

    public List<Post> getNewFeed(Long userId) {
        return postRepository.getNewFeed(userId, Message.LAST_WEEK);
    }
}
