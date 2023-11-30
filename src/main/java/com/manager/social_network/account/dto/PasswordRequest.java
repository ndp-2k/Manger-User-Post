package com.manager.social_network.account.dto;

import com.manager.social_network.user.validate.PasswordValid;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordRequest {
    @Schema(example = "nguyendangphong2712@gmail.com")
    String email;
    @PasswordValid
    @Schema(example = "DEV")
    String password;
    @Schema(example = "000000")
    String otp;
}
