package com.choing.flashbid.domain.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.choing.flashbid.domain.category.repository.CategoryRepository;
import com.choing.flashbid.global.common.error.ApiException;


@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public String getCategoryName(Long categoryId){
        return categoryRepository
                .findById(categoryId)
                .orElseThrow(()->
                        new ApiException(HttpStatus.NOT_FOUND,"존재하지 않는 카테고리","카테고리 ID :"+categoryId+"는 존재하지 않는 카테고리입니다.")
                ).getName();
    }

    public Long getCategoryId(String categoryName){
        if(categoryName==null){
            return null;
        }
        return categoryRepository
                .findByName(categoryName)
                .orElseThrow(()->new ApiException(404))
                .getId();
    }
}
