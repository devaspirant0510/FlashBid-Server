package seoil.capstone.flashbid.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/goods")
public class GoodsController {

    @PostMapping
    public String createGoods(@RequestParam("files") List<MultipartFile> files, @RequestParam("title") String title, @RequestParam("description") String description) {
        return title+","+description;

    }
}
