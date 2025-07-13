package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.auction.entity.TradingAreaEntity;

public interface TradingAreaRepository extends JpaRepository<TradingAreaEntity, Long> {
}
