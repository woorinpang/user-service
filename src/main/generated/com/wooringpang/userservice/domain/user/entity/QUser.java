package com.wooringpang.userservice.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1380625743L;

    public static final QUser user = new QUser("user");

    public final StringPath email = createString("email");

    public final StringPath googleId = createString("googleId");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath kakaoId = createString("kakaoId");

    public final DateTimePath<java.time.LocalDateTime> lastLoginDate = createDateTime("lastLoginDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> loginFailCount = createNumber("loginFailCount", Integer.class);

    public final StringPath naverId = createString("naverId");

    public final StringPath password = createString("password");

    public final StringPath refreshToken = createString("refreshToken");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final StringPath userId = createString("userId");

    public final StringPath username = createString("username");

    public final StringPath userStateCode = createString("userStateCode");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

