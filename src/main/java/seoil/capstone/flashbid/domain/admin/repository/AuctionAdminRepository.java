package seoil.capstone.flashbid.domain.admin.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seoil.capstone.flashbid.domain.admin.projection.CategoryAuctionChartProjection;
import seoil.capstone.flashbid.domain.auction.entity.Auction;

import java.util.List;

public interface AuctionAdminRepository extends JpaRepository<Auction, Long> {
    @Query(value = """
            select ac.category_id ,ca.name,ac.count
                                    from (select category_id,count(category_id) from auction group by category_id) ac
                                    join category ca
                                    on ca.id = ac.category_id
            """, nativeQuery = true)
    List<CategoryAuctionChartProjection> findCategoryAuctionCount();


}
