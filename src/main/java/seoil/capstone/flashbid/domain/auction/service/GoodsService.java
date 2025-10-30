package seoil.capstone.flashbid.domain.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.dto.response.GoodsDto;
import seoil.capstone.flashbid.domain.auction.entity.Goods;
import seoil.capstone.flashbid.domain.auction.repository.GoodsRepository;
import seoil.capstone.flashbid.domain.file.dto.SaveFileDto;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.file.repository.FileRepository;
import seoil.capstone.flashbid.domain.file.service.FileService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.enums.DeliveryType;
import seoil.capstone.flashbid.global.common.enums.FileType;
import seoil.capstone.flashbid.global.common.error.ApiException;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsService {
    private final FileService fileService;
    private final GoodsRepository goodsRepository;
    private final FileRepository fileRepository;

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
                .description(description)
                .title(title)
                .deliveryType(deliveryType)
                .build();
        Goods savedGoods = goodsRepository.save(createGoods);
        List<FileEntity> saveFileDtos = fileService.uploadAllFiles(files,account,savedGoods.getId(),FileType.GOODS);
        return new GoodsDto(savedGoods, saveFileDtos);
    }
}
