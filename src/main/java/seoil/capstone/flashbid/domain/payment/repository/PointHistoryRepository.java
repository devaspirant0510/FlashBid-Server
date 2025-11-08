package seoil.capstone.flashbid.domain.payment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.payment.entity.PointHistoryEntity;

public interface PointHistoryRepository extends JpaRepository<PointHistoryEntity, Long> {
    Slice<PointHistoryEntity> findAllByUserId_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
