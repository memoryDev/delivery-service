package com.delivery.app.domain.menu.controller;


import com.delivery.app.domain.menu.dto.request.ChangeMenuStatusRequest;
import com.delivery.app.domain.menu.dto.request.CreateMenuRequest;
import com.delivery.app.domain.menu.dto.response.MenuResponse;
import com.delivery.app.domain.menu.service.MenuService;
import com.delivery.app.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/api/owner/restaurants/{restaurantId}/menus")
    public ResponseEntity<ApiResponse<MenuResponse>> create(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal Long ownerId,
            @Valid @RequestBody CreateMenuRequest request
    ) {

        MenuResponse menuResponse = menuService.create(restaurantId, ownerId, request);

        return ResponseEntity.ok(
                ApiResponse.ok(menuResponse)
        );

    }

    @GetMapping("/api/restaurants/{restaurantId}/menus")
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getMenu(
            @PathVariable Long restaurantId
    ) {

        List<MenuResponse> menus = menuService.getMenu(restaurantId);

        return ResponseEntity.ok(
                ApiResponse.ok(menus)
        );

    }

    @PatchMapping("/api/owner/menus/{menuId}/status")
    public ResponseEntity<ApiResponse<Void>> changeStatus(
            @PathVariable Long menuId,
            @AuthenticationPrincipal Long ownerId,
            @Valid @RequestBody ChangeMenuStatusRequest request
            ) {

        menuService.changeStatus(menuId, ownerId, request);

        return ResponseEntity.ok(
                ApiResponse.ok()
        );
    }
}
