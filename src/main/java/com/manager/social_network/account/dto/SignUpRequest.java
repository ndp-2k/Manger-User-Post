package com.manager.social_network.account.dto;

import com.manager.social_network.user.validate.EmailUniqueValid;
import com.manager.social_network.user.validate.PasswordValid;
import com.manager.social_network.user.validate.UsernameUniqueValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    @NotEmpty
    @UsernameUniqueValid
    private String username;
    @NotEmpty
    @EmailUniqueValid
    @Email(message = "hãy nhập đúng định dạng email")
    private String email;
    @NotEmpty
    @PasswordValid(message = "mật khẩu phải có chứa ít nhất 6 kí tự và có chữ hoa, chữ thường, chữ số và kí tự đặc biệt")
    private String password;
}
