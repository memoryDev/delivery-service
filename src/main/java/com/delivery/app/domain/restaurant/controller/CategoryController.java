package com.delivery.app.domain.restaurant.controller;

import com.delivery.app.domain.restaurant.entity.CategoryType;
import com.delivery.app.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CategoryController {

    @GetMapping("/api/categories")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getCategories() {
        List<Map<String, String>> categories = Arrays.stream(CategoryType.values())
                .map(category -> Map.of(
                        "name", category.name(),
                        "displayName", category.getDisplayName()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.ok(categories));
    }
}
