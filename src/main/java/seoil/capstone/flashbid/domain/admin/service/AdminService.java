package seoil.capstone.flashbid.domain.admin.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import seoil.capstone.flashbid.domain.admin.projection.BidInfoProjection;
import seoil.capstone.flashbid.domain.admin.projection.CategoryAuctionChartProjection;
import seoil.capstone.flashbid.domain.admin.repository.AuctionAdminRepository;
import seoil.capstone.flashbid.domain.admin.repository.BidLogAdminRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final AuctionAdminRepository auctionAdminRepository;
    private final BidLogAdminRepository bidLogAdminRepository;
    //
    public List<CategoryAuctionChartProjection> getChartForCategoryCount(){
        return auctionAdminRepository.findCategoryAuctionCount();
    }
    public List<BidInfoProjection> getBiddingLogInfoList() {
        return bidLogAdminRepository.getBiddingLogInfoList();
    }
}
