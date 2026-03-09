package com.choing.flashbid.domain.file.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.choing.flashbid.domain.file.controller.swagger.FileSwagger;
import com.choing.flashbid.domain.file.dto.SaveFileDto;
import com.choing.flashbid.domain.file.entity.FileEntity;
import com.choing.flashbid.domain.file.service.FileService;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.global.aop.annotation.AuthUser;
import com.choing.flashbid.global.common.enums.FileType;
import com.choing.flashbid.global.common.response.ApiResult;

import java.util.List;
import java.util.Random;


@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController implements FileSwagger {
    private final FileService fileService;
    @Override
    @PostMapping("/api/files/upload")
    @Deprecated
    public ApiResult<List<SaveFileDto>> uploadFile(@RequestParam("files") List<MultipartFile> files, HttpServletRequest request) {

        return ApiResult.created(fileService.saveImage(files));
    }

    @PostMapping("/api/files/upload-v2")
    @AuthUser
    public ApiResult<List<FileEntity>> uploadFileV2(
            @RequestParam("files") List<MultipartFile> files,
            Account account
    ) {
        Random random = new Random();
        return ApiResult.created(fileService.uploadAllFiles(files,account,random.nextLong(), FileType.DM));
    }

}
