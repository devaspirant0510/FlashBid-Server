package seoil.capstone.flashbid.domain.file.controller.swagger;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.file.dto.SaveFileDto;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;

public interface FileSwagger {

    ApiResult<List<SaveFileDto>> uploadFile(List<MultipartFile> file, HttpServletRequest request);
}
