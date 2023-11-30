package com.manager.social_network.post.controller;

import com.manager.social_network.common.constan.Message;
import com.manager.social_network.common.function.Common;
import com.manager.social_network.friend.service.FriendService;
import com.manager.social_network.post.dto.CommentRequest;
import com.manager.social_network.post.dto.PostRequest;
import com.manager.social_network.post.service.CommentService;
import com.manager.social_network.post.service.LikeService;
import com.manager.social_network.post.service.PostService;
import com.manager.social_network.user.service.UserService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@RestController
@Tag(name = "Post Controller Manager")
@RequestMapping("api/v1/posts")
public class PostController {
    private Common common;
    private PostService postService;
    private UserService userService;
    private CommentService commentService;
    private FriendService friendService;
    private LikeService likeService;

    @Operation(summary = "Tạo bài viết")
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPost(
            @Valid @RequestBody @ApiParam(value = "Post Request", required = true) PostRequest post,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        postService.createPost(common.getUserIdByToken(request), post);
        response.put(Message.STATUS, Message.SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Sửa bài viết")
    @PatchMapping("/edit/{id}")
    public ResponseEntity<Map<String, Object>> editPost(
            @Valid @RequestBody @ApiParam(value = "Post Request", required = true) PostRequest post,
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

    @Operation(summary = "Xóa bài viết")
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

    @Operation(summary = "Xem bài viết")
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getPost(
            @PathVariable(name = "id") Long id,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Object response;

        if (!postService.postIsExits(id)) {
            response = Message.NOT_FOUND_POST;
        } else if (!friendService.isFriend(postService.getPostById(id).getUserId(), common.getUserIdByToken(request))) {
            response = Message.NOT_ALREADY_FRIEND;
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.OK;
            response = postService.getPostById(id);
        }
        return new ResponseEntity<>(response, status);
    }


    @Operation(summary = "Tạo bình luân")
    @PostMapping("/comment/create/{post_id}")
    public ResponseEntity<Map<String, Object>> createComment(
            @PathVariable(name = "post_id") Long postId,
            @Valid @RequestBody @ApiParam(value = "Comment Request", required = true) CommentRequest commentRequest,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        if (!postService.postIsExits(postId)) {
            status = HttpStatus.BAD_REQUEST;
            response.put(Message.ERROR, Message.NOT_FOUND_POST);
        } else if (!friendService.isFriend(postService.getPostById(postId).getUserId(), common.getUserIdByToken(request))) {
            response.put(Message.ERROR, Message.NOT_ALREADY_FRIEND);
            status = HttpStatus.BAD_REQUEST;
        } else {
            commentService.createComment(common.getUserIdByToken(request), commentRequest);
            response.put(Message.STATUS, Message.SUCCESS);
        }
        return new ResponseEntity<>(response, status);
    }


    @Operation(summary = "Sửa bình luận")
    @PatchMapping("/comment/edit/{id}")
    public ResponseEntity<Map<String, Object>> editComment(
            @Valid @RequestBody @ApiParam(value = "Comment Request", required = true) CommentRequest commentRequest,
            @PathVariable(name = "id") Long id,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.NOT_FOUND;

        if (!commentService.commentExits(id)) {
            response.put(Message.ERROR, Message.NOT_FOUND_COMMENT);
        } else if (!commentService.isValid(id, common.getUserIdByToken(request))) {
            status = HttpStatus.FORBIDDEN;
            response.put(Message.ERROR, Message.NOT_ALLOWED);
        } else {
            status = HttpStatus.OK;
            commentService.updateComment(id, commentRequest);
            response.put(Message.STATUS, Message.SUCCESS);
        }
        return new ResponseEntity<>(response, status);
    }

    @Operation(summary = "Xem bình luận")
    @GetMapping("/comment/get/{id}")
    public ResponseEntity<Object> getComment(
            @PathVariable(name = "id") Long id,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Object response;

        if (!commentService.commentExits(id)) {
            response = Message.NOT_FOUND_COMMENT;
        } else if (!friendService.isFriend(commentService.getCommentById(id).getUserId(), common.getUserIdByToken(request))) {
            response = Message.NOT_ALREADY_FRIEND;
        } else {
            status = HttpStatus.OK;
            response = commentService.getCommentById(id);
        }
        return new ResponseEntity<>(response, status);
    }

    @Operation(summary = "Like post")
    @GetMapping("/like/{post_id}")
    public ResponseEntity<Object> post(
            @PathVariable(name = "post_id") Long postId,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        if (!postService.postIsExits(postId)) {
            status = HttpStatus.BAD_REQUEST;
            response.put(Message.ERROR, Message.NOT_FOUND_POST);
            return new ResponseEntity<>(response, status);
        }
        Long userId = common.getUserIdByToken(request);
        Long userPostId = postService.getPostById(postId).getUserId();
        if (Objects.equals(userId, userPostId) || friendService.isFriend(userId, userPostId)) {
            if (likeService.likePost(userId, postId) == 0) {
                response.put(Message.STATUS, Message.UN_LIKE_POST);
            } else {
                response.put(Message.STATUS, Message.LIKE_POST);
            }
        } else {
            response.put(Message.ERROR, Message.NOT_ALREADY_FRIEND);
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(response, status);
    }
}
