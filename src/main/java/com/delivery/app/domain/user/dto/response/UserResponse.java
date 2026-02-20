package com.delivery.app.domain.user.dto.response;

import com.delivery.app.domain.user.entity.Role;
import com.delivery.app.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String email;
    private final String name;
    private final String phone;
    private final String address;
    private final Role role;

    private UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.role = user.getRole();
    }

    // Entity -> dto 변환
    public static UserResponse from(User user) {
        return new UserResponse(user);
    }
}
