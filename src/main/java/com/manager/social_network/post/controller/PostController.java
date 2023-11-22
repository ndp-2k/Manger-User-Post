package com.manager.social_network.post.controller;

import com.manager.social_network.common.constan.Message;
import com.manager.social_network.common.function.Common;
import com.manager.social_network.post.dto.CommentRequest;
import com.manager.social_network.post.dto.PostRequest;
import com.manager.social_network.post.service.CommentService;
import com.manager.social_network.post.service.PostService;
import com.manager.social_network.user.service.UserService;
import io.swagger.annotations.ApiParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/posts")
public class PostController {
    private Common common;
    private PostService postService;
    private UserService userService;
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPost(
            @Valid @RequestBody @ApiParam(value = "Post Request",required = true) PostRequest post,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        postService.createPost(common.getUsernameByToken(request), post);
        response.put(Message.STATUS, Message.SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Map<String, Object>> editPost(
            @Valid @RequestBody @ApiParam(value = "Post Request",required = true) PostRequest post,
            @PathVariable(name = "id") Long id,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        if (!postService.valid(id, common.getUserIdByToken(request))) {
            response.put(Message.ERROR, Message.NOT_FOUND_POST);
            status = HttpStatus.BAD_REQUEST;
        } else {
            postService.updatePost(id, post);
            response.put(Message.STATUS, Message.SUCCESS);
        }
        return new ResponseEntity<>(response, status);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deletePost(
            @PathVariable(name = "id") Long id,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        if (!postService.valid(id, common.getUserIdByToken(request))) {
            response.put(Message.ERROR, Message.NOT_FOUND_POST);
            status = HttpStatus.BAD_REQUEST;
        } else {
            postService.deletePost(id);
            response.put(Message.STATUS, Message.SUCCESS);
        }
        return new ResponseEntity<>(response, status);

    }

    @PostMapping("/get/{id}")
    public ResponseEntity<Object> getPost(
            @PathVariable(name = "id") Long id
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Object response;

        if (!postService.postIsExits(id)) {
            response = Message.NOT_FOUND_POST;
        } else {
            status = HttpStatus.OK;
            response = postService.getPostById(id);
        }
        return new ResponseEntity<>(response, status);
    }


    @PostMapping("/comment/create/{post_id}")
    public ResponseEntity<Map<String, Object>> createComment(
            @PathVariable(name = "post_id") Long postId,
            @Valid @RequestBody @ApiParam(value = "Comment Request",required = true) CommentRequest commentRequest,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        if (!postService.postIsExits(postId)) {
            status = HttpStatus.BAD_REQUEST;
            response.put(Message.ERROR, Message.NOT_FOUND_POST);
        } else {
            commentService.createComment(common.getUserIdByToken(request), commentRequest);
            response.put(Message.STATUS, Message.SUCCESS);
        }
        return new ResponseEntity<>(response, status);
    }


    @PostMapping("/comment/edit/{id}")
    public ResponseEntity<Map<String, Object>> editComment(
            @Valid @RequestBody @ApiParam(value = "Comment Request",required = true) CommentRequest commentRequest,
            @PathVariable(name = "id") Long id,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (!commentService.commentExits(id)) {
            response.put(Message.ERROR, Message.NOT_FOUND_COMMENT);
        } else if (commentService.isValid(id, common.getUserIdByToken(request))) {
            status = HttpStatus.FORBIDDEN;
            response.put(Message.ERROR, Message.NOT_ALLOWED);
        } else {
            status = HttpStatus.OK;
            commentService.updateComment(id, commentRequest);
            response.put(Message.STATUS, Message.SUCCESS);
        }
        return new ResponseEntity<>(response, status);
    }

    @PostMapping("/comment/get/{id}")
    public ResponseEntity<Object> getComment(
            @PathVariable(name = "id") Long id
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Object response;

        if (!commentService.commentExits(id)) {
            response = Message.NOT_FOUND_COMMENT;
        } else {
            status = HttpStatus.OK;
            response = commentService.getCommentById(id);
        }
        return new ResponseEntity<>(response, status);
    }

}
