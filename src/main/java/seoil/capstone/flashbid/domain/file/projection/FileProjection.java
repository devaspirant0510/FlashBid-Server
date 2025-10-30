package seoil.capstone.flashbid.domain.file.projection;

import seoil.capstone.flashbid.global.common.enums.FileType;

public interface FileProjection {
    Long getId();
    String getUrl();
    Long getFileId();
    FileType getFileType();
}
