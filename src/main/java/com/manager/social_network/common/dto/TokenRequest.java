package com.manager.social_network.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenRequest {

    @Schema(example = "phong")
    private String username;
    @Schema(example = "Phongtom#1")
    private String password;
    @Schema(example = "000000")
    private String otp;

}