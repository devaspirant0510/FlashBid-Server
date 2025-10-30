package seoil.capstone.flashbid.domain.dm.entity;

import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dm_image")
public class DMImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 파일 ID 참조 (이미 파일 테이블이 있을 경우)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileEntity file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dm_id")
    private DMChat dmChat;
}
