package seoil.capstone.flashbid.domain.feed.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.entity.Goods;
import seoil.capstone.flashbid.domain.auction.service.GoodsService;
import seoil.capstone.flashbid.domain.feed.controller.FeedController;
import seoil.capstone.flashbid.domain.feed.dto.request.CreateFeedDto;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedDto;
import seoil.capstone.flashbid.domain.feed.entity.FeedEntity;
import seoil.capstone.flashbid.domain.feed.repository.FeedRepository;
import seoil.capstone.flashbid.domain.file.dto.SaveFileDto;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.file.service.FileService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.enums.FileType;
import seoil.capstone.flashbid.global.common.error.ApiException;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {
    private final FeedRepository feedRepository;
    private final FileService fileService;

    @Transactional
    public FeedDto createFeed(Account account, List<MultipartFile> files, CreateFeedDto dto){
        FeedEntity feedEntity = FeedEntity
                .builder()
                .contents(dto.getContent())
                .user(account)
                .viewCount(0)
                .build();
        FeedEntity savedEntity = feedRepository.save(feedEntity);
        List<SaveFileDto> saveFileDtos = fileService.saveImage(files);
        List<FileEntity> saveFileEntities = fileService.saveFileEntities(saveFileDtos,feedEntity.getId(),account, FileType.FEED);
        return new FeedDto(
                savedEntity,
                saveFileEntities
        );
    }

    public FeedDto getFeedById(Long id){
        FeedEntity feedEntity = feedRepository.findById(id).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "404E00F00", "존재하지 않는 피드입니다.")
        );
        return new FeedDto(
                feedEntity,
                List.of()
        );
    }

}
