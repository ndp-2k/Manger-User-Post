package com.manager.social_network.account.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailRequest {
    @Email(message = "Email không đúng định dạng")
    @Schema(example = "nguyendangphong2712@gmail.com")
    private String email;

}