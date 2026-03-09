package com.choing.flashbid.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.choing.flashbid.domain.auth.entity.UserFcmEntity;

import java.util.Optional;

public interface UserFcmRepository extends JpaRepository<UserFcmEntity,Long> {
    Optional<UserFcmEntity> findByAccountId(Long userId);
}
