package com.choing.flashbid.domain.file.controller.swagger;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import com.choing.flashbid.domain.file.dto.SaveFileDto;
import com.choing.flashbid.global.common.response.ApiResult;

import java.util.List;

public interface FileSwagger {

    ApiResult<List<SaveFileDto>> uploadFile(List<MultipartFile> file, HttpServletRequest request);
}
