package com.choing.flashbid.domain.feed.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.choing.flashbid.domain.feed.entity.FeedEntity;
import com.choing.flashbid.domain.file.entity.FileEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedDtoLegacy {
    private FeedEntity feed;
    private List<FileEntity> images;
    private int commentCount;
    private int likeCount;
    private boolean isLiked;
}
