package com.choing.flashbid.domain.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.choing.flashbid.domain.auction.dto.response.GoodsDto;
import com.choing.flashbid.domain.auction.entity.Goods;
import com.choing.flashbid.domain.auction.repository.jpa.GoodsRepository;
import com.choing.flashbid.domain.file.entity.FileEntity;
import com.choing.flashbid.domain.file.repository.FileRepository;
import com.choing.flashbid.domain.file.service.FileService;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.global.common.enums.DeliveryType;
import com.choing.flashbid.global.common.enums.FileType;
import com.choing.flashbid.global.common.error.ApiException;
import com.choing.flashbid.infrastructure.id.SnowflakeGenerator;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsService {
    private final FileService fileService;
    private final GoodsRepository goodsRepository;
    private final FileRepository fileRepository;
    private final SnowflakeGenerator generator;

    @Transactional
    public GoodsDto getGoodsById(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "", ""));
        List<FileEntity> allFiles = fileService.getAllFiles(goodsId, FileType.GOODS);
        return new GoodsDto(goods,allFiles);
    }

    @Transactional
    public GoodsDto uploadGoods(Account account, List<MultipartFile> files, String title, String description,DeliveryType deliveryType) {
        Goods createGoods = Goods
                .builder()
                .id(generator.nextId())
                .description(description)
                .title(title)
                .deliveryType(deliveryType)
                .build();
        Goods savedGoods = goodsRepository.save(createGoods);
        List<FileEntity> saveFileDtos = fileService.uploadAllFiles(files,account,savedGoods.getId(),FileType.GOODS);
        return new GoodsDto(savedGoods, saveFileDtos);
    }
}
