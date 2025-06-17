package seoil.capstone.flashbid.domain.file.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.global.common.enums.FileType;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity,Long> {
    List<FileEntity> findAllByFileIdAndFileType(Long fileId, FileType fileType);
}
