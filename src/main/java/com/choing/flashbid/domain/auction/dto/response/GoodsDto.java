package com.choing.flashbid.domain.auction.dto.response;


import lombok.*;
import com.choing.flashbid.domain.auction.entity.Goods;
import com.choing.flashbid.domain.file.entity.FileEntity;

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
