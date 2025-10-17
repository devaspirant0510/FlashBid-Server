package seoil.capstone.flashbid.infrastructure.file;


import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public abstract sealed class FileUploader permits LocalFileUploader, S3FileUploader {
    abstract public UploadResult uploadFile(MultipartFile file);

    protected String extractExtension(String originalFilename) {
        return StringUtils.getFilenameExtension(originalFilename);
    }

    protected String removeExtension(String originalFilename) {
        return StringUtils.stripFilenameExtension(originalFilename);
    }


}
