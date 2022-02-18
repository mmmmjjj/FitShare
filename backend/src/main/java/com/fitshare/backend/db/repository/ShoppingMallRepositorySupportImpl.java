package com.fitshare.backend.db.repository;

import com.fitshare.backend.db.entity.ShoppingMall;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.fitshare.backend.db.entity.QShoppingMall.*;

public class ShoppingMallRepositorySupportImpl extends QuerydslRepositorySupport implements ShoppingMallRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public ShoppingMallRepositorySupportImpl(JPAQueryFactory queryFactory) {
        super(ShoppingMall.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<ShoppingMall> findShoppingMallsByNameContainingOrUrlContaining(String keyword) {
        return queryFactory
                .selectFrom(shoppingMall)
                .where(nameAndUrlContains(keyword))
                .fetch();
    }

    private BooleanExpression nameAndUrlContains(String keyword) {
        return keyword != null ? nameContains(keyword).or(urlContains(keyword)) : null;
    }

    private BooleanExpression nameContains(String keyword) {
        return keyword != null ? shoppingMall.name.contains(keyword) : null;
    }

    private BooleanExpression urlContains(String keyword) {
        return keyword != null ? shoppingMall.url.contains(keyword) : null;
    }
}
