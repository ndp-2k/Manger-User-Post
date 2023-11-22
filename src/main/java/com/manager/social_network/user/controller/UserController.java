package com.manager.social_network.user.controller;

import com.manager.social_network.common.function.Common;
import com.manager.social_network.common.jwt.JwtService;
import com.manager.social_network.user.dto.UserRequest;
import com.manager.social_network.user.service.UserService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Tag(name = "User Controller Manager")
@RequestMapping("api/v1/users/")
public class UserController {
    UserService userService;
    private Common common;

    @PutMapping("/edit")
    @Operation(summary = "Cập nhật thông tin người dùng")
    @PermitAll
    public String edit(@Valid @RequestBody @ApiParam(value = "User Request", required = true) UserRequest userRequest, HttpServletRequest request) throws Throwable {
        userService.saveUser(common.getUsernameByToken(request), userRequest);
        return "Success";
    }
}
