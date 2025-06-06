package seoil.capstone.flashbid.domain.file.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity,Long> {
}
