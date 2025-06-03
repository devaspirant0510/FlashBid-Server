package seoil.capstone.flashbid.domain.file.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.file.controller.swagger.FileSwagger;
import seoil.capstone.flashbid.domain.file.dto.SaveFileDto;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.file.service.FileService;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController implements FileSwagger {
    private final FileService fileService;
    @Override
    @PostMapping("/api/files/upload")
    public ApiResult<List<SaveFileDto>> uploadFile(@RequestParam("files") List<MultipartFile> files, HttpServletRequest request) {

        return ApiResult.created(fileService.saveImage(files,1L),request);
    }
}
