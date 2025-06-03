package seoil.capstone.flashbid.domain.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.file.dto.SaveFileDto;
import seoil.capstone.flashbid.global.common.error.ApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final String uploadDir = System.getProperty("user.home") + "/seungho/uploads/";

    public List<SaveFileDto> saveImage(List<MultipartFile> files, Long uid) {
        List<SaveFileDto> fileNames = new ArrayList<>();
        createOrGetDirectory();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 읽기 에러", "500E001");
            }
            String fileName = new Date().toString() + file.getOriginalFilename();
            String[] extension = fileName.split("\\.");
            System.out.println(Arrays.toString(extension));

            fileNames.add(SaveFileDto
                    .builder()
                    .fileName(fileName)
                    .extension(extension[extension.length - 1])
                    .build());
            try{
                file.transferTo(new File(uploadDir+fileName));
            }catch (Exception e){
                log.error("file upload error");
                e.printStackTrace();
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 에러", "500E001");
            }
        }
        return fileNames;
    }

    private void createOrGetDirectory() {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러", "500E001");
            }
        }
    }
}
