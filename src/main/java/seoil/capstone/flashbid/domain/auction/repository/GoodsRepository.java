package seoil.capstone.flashbid.domain.auction.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.auction.entity.Goods;

public interface GoodsRepository extends JpaRepository<Goods,Long> {
}
