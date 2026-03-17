package com.delivery.app.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String phone;

    private String address;
}
