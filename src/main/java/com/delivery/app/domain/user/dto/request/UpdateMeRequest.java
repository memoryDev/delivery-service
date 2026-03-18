package com.delivery.app.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateMeRequest {

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "전화번호를 입력해 주세요.")
    @Pattern(regexp = "^(02|\\d{3})-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)")
    private String phone;

    private String address;
}
