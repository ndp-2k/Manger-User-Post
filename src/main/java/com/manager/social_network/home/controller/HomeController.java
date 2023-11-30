package com.manager.social_network.home.controller;

import com.manager.social_network.common.function.Common;
import com.manager.social_network.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("home")
public class HomeController {
    Common common;
    PostService postService;

    @GetMapping("new-feed")
    public ResponseEntity<Object> newFeed(HttpServletRequest request) {
        return new ResponseEntity<>(postService.getNewFeed(common.getUserIdByToken(request)), HttpStatus.OK);
    }
}
