package com.choing.flashbid.domain.admin.repository.querydsl;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.choing.flashbid.domain.user.dto.response.AccountDetailDto;
import com.choing.flashbid.domain.user.entity.QAccount;
import com.choing.flashbid.global.common.enums.UserStatus;

import java.util.List;

@RequiredArgsConstructor
public class AccountAdminQueryRepositoryImpl implements AccountAdminQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AccountDetailDto> findAccounts(UserStatus status, Pageable pageable) {
        QAccount account = QAccount.account;
        List<AccountDetailDto> content =
                queryFactory
                        .select(Projections.fields(
                                AccountDetailDto.class,
                                account.id,
                                account.email,
                                account.nickname,
                                account.userStatus,
                                account.userType,
                                account.loginType,
                                account.deletedAt,
                                account.isVerified,
                                account.uuid,
                                account.description,
                                account.profileUrl,
                                account.point
                        ))
                        .from(account)
                        .where(
                                userStatusEq(status)
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();
        Long total =
                queryFactory
                        .select(account.count())
                        .from(account)
                        .where(
                                userStatusEq(status)
                        )
                        .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private BooleanExpression userStatusEq(UserStatus userStatus) {
        return userStatus == null
                ? null
                : QAccount.account.userStatus.eq(userStatus);
    }
}
