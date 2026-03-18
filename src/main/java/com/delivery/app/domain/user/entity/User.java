package com.delivery.app.domain.user.entity;

import com.delivery.app.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    private User(String email, String password, String name, String phone, String address, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    // 객체 생성시 사용
    public static User create(String email, String password, String name, String phone, String address, Role role) {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .address(address)
                .role(role)
                .build();
    }
    
    // 프로필 수정
    public void updateProfile(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    // 비밀번호 변경
    public void updatePassword(String password) {
        this.password = password;
    }

}
