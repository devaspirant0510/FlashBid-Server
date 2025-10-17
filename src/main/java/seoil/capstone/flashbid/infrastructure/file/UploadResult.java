package seoil.capstone.flashbid.infrastructure.file;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class UploadResult {
    private final String url;          // file:// URL or path
    private final String storedName;   // 실제 저장된 파일명 (확장자 포함)
    private final String extension;    // 확장자 (없으면 빈 문자열)
}
