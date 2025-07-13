package seoil.capstone.flashbid.domain.category.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import seoil.capstone.flashbid.domain.category.entity.CategoryEntity;
import seoil.capstone.flashbid.domain.category.repository.CategoryRepository;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryRepository repository;

    @GetMapping()
    public ApiResult<List<CategoryEntity>> getAllCategory(
            HttpServletRequest request
    ){
        return ApiResult.ok(repository.findAllByRootIdIsNull(),request);
    }
}
