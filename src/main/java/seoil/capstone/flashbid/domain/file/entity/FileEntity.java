package seoil.capstone.flashbid.domain.file.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.enums.FileType;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "file")
public class FileEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account userId;

    @Column
    private String fileName;

    @Column
    private String extension;

    @Column
    private String url;

    @Enumerated()
    @Column(name = "file_type", nullable = false, length = 20)
    private FileType fileType;

    @Column
    private Long fileId;


}
