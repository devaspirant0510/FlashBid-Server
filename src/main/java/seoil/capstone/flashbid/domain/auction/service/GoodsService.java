package seoil.capstone.flashbid.domain.auction.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import seoil.capstone.flashbid.domain.auction.entity.Goods;
import seoil.capstone.flashbid.domain.file.service.FileService;


@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsService {
    private final FileService fileService;

}
