package seoil.capstone.flashbid.domain.admin.repository.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import seoil.capstone.flashbid.domain.user.dto.response.AccountDetailDto;
import seoil.capstone.flashbid.global.common.enums.UserStatus;

public interface AccountAdminQueryRepository {
    Page<AccountDetailDto> findAccounts(
            UserStatus status,
            Pageable pageable
    );
}
