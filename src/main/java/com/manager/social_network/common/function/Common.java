package com.manager.social_network.common.function;

import com.manager.social_network.common.jwt.JwtService;
import com.manager.social_network.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class Common {


    private JwtService jwtService;
    private UserService userService;

    public String getUsernameByToken(HttpServletRequest request) {
        return jwtService.extractUsername(request.getHeader("Authorization").substring(7));
    }

    public Long getUserIdByToken(HttpServletRequest request) {
        if (userService.findByUsername(getUsernameByToken(request)).isEmpty()){
            return null;
        }
        return userService.findByUsername(getUsernameByToken(request)).get().getId();
    }

}
