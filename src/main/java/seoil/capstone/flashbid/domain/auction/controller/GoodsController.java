package seoil.capstone.flashbid.domain.auction.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.dto.response.GoodsDto;
import seoil.capstone.flashbid.domain.auction.service.GoodsService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.auction.controller.swagger.GoodsSwagger;
import seoil.capstone.flashbid.global.aop.annotation.AuthUser;
import seoil.capstone.flashbid.global.common.enums.DeliveryType;
import seoil.capstone.flashbid.global.common.response.ApiResult;

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
            @RequestParam("description") String description,
            HttpServletRequest request
    ) {
        GoodsDto goodsDto = goodsService.uploadGoods(user, files, title, description, DeliveryType.DIRECT);
        return ApiResult.created(goodsDto,request);
    }
}
