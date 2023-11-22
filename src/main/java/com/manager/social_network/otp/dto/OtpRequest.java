package com.manager.social_network.otp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequest  {
    @Schema(example = "phong")
    private String username;
    @Schema(example = "Phongtom#1")
    private String password;
}