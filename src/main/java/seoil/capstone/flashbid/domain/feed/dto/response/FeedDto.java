package seoil.capstone.flashbid.domain.feed.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seoil.capstone.flashbid.domain.feed.entity.FeedEntity;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedDto {
    private FeedEntity feed;
    private List<FileEntity> images;
    private int commentCount;
    private int likeCount;

}
