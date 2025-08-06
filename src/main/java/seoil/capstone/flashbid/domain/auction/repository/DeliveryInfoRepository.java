package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.auction.entity.DeliveryInfoEntity;

public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfoEntity, Long> {
}
