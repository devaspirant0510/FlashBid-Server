package seoil.capstone.flashbid.infrastructure.file;


import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import seoil.capstone.flashbid.global.common.error.ApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public final class LocalFileUploader extends FileUploader {
    @Override
    public UploadResult uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "업로드 실패", "업로드할 파일이 비어있습니다.");
        }
        Path uploadDir = Paths.get(System.getProperty("user.home"),"unknown-upload");
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e){
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "업로드 실패", "업로드 디렉토리 생성 실패");
        }
        // 파일명 및 확장자 처리
        String originalName = file.getOriginalFilename();
        String originalExt = extractExtension(originalName);
        String desiredName = removeExtension(originalName);
        if (desiredName == null || desiredName.isBlank()) desiredName = UUID.randomUUID().toString();
        String finalFileName = desiredName + "-" + UUID.randomUUID()+"."+originalName;
        Path destination = uploadDir.resolve(finalFileName);
        try{
            Files.copy(file.getInputStream(), destination);
        } catch (IOException e){
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "업로드 실패", "파일 저장 실패");
        }
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()+"/uploads/"+finalFileName;
        return new UploadResult(url,finalFileName,originalExt);
    }
}
