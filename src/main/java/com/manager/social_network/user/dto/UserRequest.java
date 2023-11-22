package com.manager.social_network.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
public class UserRequest {
    @NotEmpty(message = "Tên không được trống")
    @Schema(example = "Nguyễn Đăng Phòng")
    private String fullName;
    @Schema(example = "2000-12-27")
    private Date birthday;
    @Schema(example = "DEV")
    private String job;
    @Schema(example = "VietNam")
    private String living;

}
