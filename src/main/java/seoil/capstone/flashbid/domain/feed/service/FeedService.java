package seoil.capstone.flashbid.domain.feed.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.feed.dto.request.CreateCommentDto;
import seoil.capstone.flashbid.domain.feed.dto.request.CreateFeedDto;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedDto;
import seoil.capstone.flashbid.domain.feed.entity.CommentEntity;
import seoil.capstone.flashbid.domain.feed.entity.FeedEntity;
import seoil.capstone.flashbid.domain.feed.entity.LikeEntity;
import seoil.capstone.flashbid.domain.feed.repository.CommentRepository;
import seoil.capstone.flashbid.domain.feed.repository.FeedRepository;
import seoil.capstone.flashbid.domain.feed.repository.LikeRepository;
import seoil.capstone.flashbid.domain.file.dto.SaveFileDto;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.file.service.FileService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.enums.FileType;
import seoil.capstone.flashbid.global.common.error.ApiException;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {
    private final FeedRepository feedRepository;
    private final FileService fileService;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public FeedDto createFeed(Account account, List<MultipartFile> files, CreateFeedDto dto) {
        FeedEntity feedEntity = FeedEntity
                .builder()
                .contents(dto.getContent())
                .user(account)
                .viewCount(0)
                .build();
        FeedEntity savedEntity = feedRepository.save(feedEntity);
        List<SaveFileDto> saveFileDtos = fileService.saveImage(files);
        List<FileEntity> saveFileEntities = fileService.saveFileEntities(saveFileDtos, feedEntity.getId(), account, FileType.FEED);
        return new FeedDto(
                savedEntity,
                saveFileEntities,
                0,
                0
        );
    }

    public FeedEntity fetchFeedById(Long id){
        return feedRepository.findById(id).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "404E00F00", "존재하지 않는 피드입니다.")
        );
    }



    @Transactional
    public FeedDto getFeedById(Long id) {
        FeedEntity feedEntity = fetchFeedById(id);
        int commentCount = commentRepository.countByFeedId(id);
        int likeCount = likeRepository.countByFeedId(id);
        return new FeedDto(
                feedEntity,
                List.of(),
                commentCount,
                likeCount
        );
    }

    public List<FeedDto> getTestAllFeed() {
        List<FeedDto> feedDtoList = new ArrayList<>();

        feedRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).forEach(feed -> {
            feedDtoList.add(getFeedById(feed.getId()));
        });
        return feedDtoList;
    }

    public LikeEntity likePost(Account user, Long feedId) {
        FeedEntity feedEntity = feedRepository.findById(feedId).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "400E00F001", "존재하지 않는 게시글입니다."));

        LikeEntity like = LikeEntity.builder()
                .account(user)
                .feed(feedEntity)
                .build();
        return likeRepository.save(like);
    }

    @Transactional
    public Boolean unLikePost(Account user,Long feedId){
        int deleteCount = likeRepository.deleteByFeedIdAndAccountId(feedId,user.getId());
        log.info(deleteCount+" ");
        if(deleteCount==1){
            return true;
        }
        return false;
    }

    public CommentEntity createComent(Account user, Long feedId, CreateCommentDto dto){
        FeedEntity feedEntity = feedRepository.findById(feedId).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "404E00F00", "존재하지 않는 피드입니다.")
        );
        CommentEntity comment = null;
        if(dto.getCommentId()!=null){
            comment = commentRepository.findById(dto.getCommentId()).orElseThrow(
                    ()-> new ApiException(HttpStatus.NOT_FOUND,"404E00C00","존재하지 않는 댓글입니다.")
            );
        }
        CommentEntity createComment = CommentEntity.builder()
                .contents(dto.getContents())
                .user(user)
                .feed(feedEntity)
                .reply(comment)
                .build();
        return commentRepository.save(createComment);
    }

    public List<CommentEntity> getAllRootComment(Long feedId){
        return commentRepository.findAllByFeedIdAndReplyIsNull(feedId);
    }
    public List<CommentEntity> getAllCommentByReplyId(Long replyId){
        return commentRepository.findAllByFeedIdAndReplyIsNull(replyId);
    }
}
