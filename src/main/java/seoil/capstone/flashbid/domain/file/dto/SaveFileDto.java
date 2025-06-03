package seoil.capstone.flashbid.domain.file.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveFileDto {
    private String fileName;
    private String extension;
}
