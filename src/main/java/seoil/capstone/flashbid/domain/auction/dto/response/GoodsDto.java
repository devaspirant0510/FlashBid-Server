package seoil.capstone.flashbid.domain.auction.dto.response;


import lombok.*;
import seoil.capstone.flashbid.domain.auction.entity.Goods;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDto {
    private Goods goods;
    private List<FileEntity> files;

}
