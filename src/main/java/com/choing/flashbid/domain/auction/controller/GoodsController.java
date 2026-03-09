package com.choing.flashbid.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.choing.flashbid.domain.auction.controller.swagger.GoodsSwagger;
import com.choing.flashbid.domain.auction.dto.response.GoodsDto;
import com.choing.flashbid.domain.auction.service.GoodsService;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.global.aop.annotation.AuthUser;
import com.choing.flashbid.global.common.enums.DeliveryType;
import com.choing.flashbid.global.common.response.ApiResult;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/goods")
public class GoodsController implements GoodsSwagger {
    private final GoodsService goodsService;

    @Override
    @PostMapping
    @AuthUser
    public ApiResult<GoodsDto> createGoods(
            Account user,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("title") String title,
            @RequestParam("description") String description
    ) {
        GoodsDto goodsDto = goodsService.uploadGoods(user, files, title, description, DeliveryType.DIRECT);
        return ApiResult.created(goodsDto);
    }
}
