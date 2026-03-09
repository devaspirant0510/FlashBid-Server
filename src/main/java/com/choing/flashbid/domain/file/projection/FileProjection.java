package com.choing.flashbid.domain.file.projection;

import com.choing.flashbid.global.common.enums.FileType;

public interface FileProjection {
    Long getId();
    String getUrl();
    Long getFileId();
    FileType getFileType();
}
