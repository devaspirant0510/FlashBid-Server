package com.choing.flashbid.domain.auction.repository.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import com.choing.flashbid.domain.auction.entity.AuctionEventLogEntity;

public interface AuctionEventLogRepository extends JpaRepository<AuctionEventLogEntity,Long> {
}
