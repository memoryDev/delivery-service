package com.delivery.app.domain.menu.service;

import com.delivery.app.domain.menu.dto.request.CreateMenuRequest;
import com.delivery.app.domain.menu.dto.response.MenuResponse;
import com.delivery.app.domain.menu.entity.Menu;
import com.delivery.app.domain.menu.repository.MenuRepository;
import com.delivery.app.domain.restaurant.entity.Restaurant;
import com.delivery.app.domain.restaurant.repository.RestaurantRepository;
import com.delivery.app.domain.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 등록 성공 테스트")
    public void createMenu_success() {
        // given
        Long restaurantId = 1L;
        Long ownerId = 1L;
        CreateMenuRequest createMenuRequest = new CreateMenuRequest("황금올리브 치킨", "바삭바삭후라이드", 20000);

        User mockOwner = Mockito.mock(User.class);
        BDDMockito.given(mockOwner.getId()).willReturn(ownerId); // 내식당이 맞도록

        Restaurant mockRestaurant = Mockito.mock(Restaurant.class);
        BDDMockito.given(mockRestaurant.getOwner()).willReturn(mockOwner);

        BDDMockito.given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(mockRestaurant));

        BDDMockito.given(menuRepository.save(ArgumentMatchers.any(Menu.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        MenuResponse response = menuService.create(restaurantId, ownerId, createMenuRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("황금올리브 치킨", response.getName());
        Assertions.assertEquals(20000, response.getPrice());
        Assertions.assertEquals(createMenuRequest.getDescription(), response.getDescription());
    }

}