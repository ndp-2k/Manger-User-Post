package com.manager.social_network.home.controller;

import com.manager.social_network.common.function.Common;
import com.manager.social_network.post.entity.Post;
import com.manager.social_network.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("home")
public class HomeController {
    Common common;
    PostService postService;
    @GetMapping("new-feed")
    public ResponseEntity<Object> newFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Post> items = postService.getNewFeed(common.getUserIdByToken(request), pageable);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
