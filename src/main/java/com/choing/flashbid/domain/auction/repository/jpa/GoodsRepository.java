package com.choing.flashbid.domain.auction.repository.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import com.choing.flashbid.domain.auction.entity.Goods;

public interface GoodsRepository extends JpaRepository<Goods,Long> {
}
