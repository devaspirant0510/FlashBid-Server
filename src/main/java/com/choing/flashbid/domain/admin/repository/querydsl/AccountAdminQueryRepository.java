package com.choing.flashbid.domain.admin.repository.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.choing.flashbid.domain.user.dto.response.AccountDetailDto;
import com.choing.flashbid.global.common.enums.UserStatus;

public interface AccountAdminQueryRepository {
    Page<AccountDetailDto> findAccounts(
            UserStatus status,
            Pageable pageable
    );
}
