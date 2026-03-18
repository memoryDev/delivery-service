package com.delivery.app.domain.menu.repository;

import com.delivery.app.domain.menu.entity.Menu;
import com.delivery.app.domain.menu.entity.QMenu;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuRepositoryCustomImpl implements MenuRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Menu> findMenusByRestaurantId(Long restaurantId) {

        QMenu menu = QMenu.menu;

        return queryFactory.selectFrom(menu)
                .where(
                        menu.restaurant.id.eq(restaurantId)
                )
                .orderBy(
                        menu.status.asc(),
                        menu.id.desc()
                ).fetch();
    }
}
