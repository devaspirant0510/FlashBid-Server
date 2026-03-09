package com.choing.flashbid.domain.file.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.choing.flashbid.domain.file.entity.FileEntity;
import com.choing.flashbid.domain.file.projection.FileProjection;
import com.choing.flashbid.global.common.enums.FileType;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findAllByFileIdAndFileType(Long fileId, FileType fileType);

    void deleteByFileId(Long fileId);

    @Query("""
                    select
                    f.id as id,
                    f.url as url,
                    f.fileId as fileId,
                    f.fileType as fileType
                    from file f
                    where f.fileId in :ids and f.fileType = :fileType
            """)
    List<FileProjection> findAllInFileIdsWithFileType(List<Long> ids, FileType fileType);
}
