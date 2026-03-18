package com.delivery.app.domain.restaurant.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {

    KOREAN("한식"),
    CHINESE("중식"),
    WESTERN("양식"),
    JAPANESE("일식"),
    CHICKEN("치킨"),
    PIZZA("피자"),
    BUNSIK("분식"),
    CAFE("카페/디저트"),
    ETC("기타");

    private final String displayName;
}
