package com.manager.social_network.user.controller;

import com.manager.social_network.common.function.Common;
import com.manager.social_network.common.jwt.JwtService;
import com.manager.social_network.user.dto.UserRequest;
import com.manager.social_network.user.service.UserService;
import io.swagger.annotations.ApiParam;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/users/")
public class UserController {
    UserService userService;
    private Common common;
    private JwtService jwtService;

    @PostMapping("/edit")
    @PermitAll
    public String edit(@Valid @RequestBody @ApiParam(value = "User Request", required = true) UserRequest userRequest, HttpServletRequest request) throws Throwable {
        userService.saveUser(common.getUsernameByToken(request), userRequest);
        return "Success";
    }
}
