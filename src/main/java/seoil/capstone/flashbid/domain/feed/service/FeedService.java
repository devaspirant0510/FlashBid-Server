package seoil.capstone.flashbid.domain.feed.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.feed.dto.request.CreateCommentDto;
import seoil.capstone.flashbid.domain.feed.dto.request.CreateFeedDto;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedDto;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedListResponse;
import seoil.capstone.flashbid.domain.feed.entity.CommentEntity;
import seoil.capstone.flashbid.domain.feed.entity.FeedEntity;
import seoil.capstone.flashbid.domain.feed.entity.LikeEntity;
import seoil.capstone.flashbid.domain.feed.projection.FeedProjection;
import seoil.capstone.flashbid.domain.feed.repository.CommentRepository;
import seoil.capstone.flashbid.domain.feed.repository.FeedRepository;
import seoil.capstone.flashbid.domain.feed.repository.LikeRepository;
import seoil.capstone.flashbid.domain.file.dto.SaveFileDto;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.file.projection.FileProjection;
import seoil.capstone.flashbid.domain.file.repository.FileRepository;
import seoil.capstone.flashbid.domain.file.service.FileService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.enums.FileType;
import seoil.capstone.flashbid.global.common.error.ApiException;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {
    private final FeedRepository feedRepository;
    private final FileService fileService;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final FileRepository fileRepository;

    @Transactional(readOnly = true)
    public Slice<FeedListResponse> getFeedQuery(int page, int size, Account account) {
        // Feed 조회
        Slice<FeedProjection> allFeedQuery = feedRepository.findAllFeedQuery(PageRequest.of(page, size), account == null ? null : account.getId());
        // Feed ID 추출후 해당 피드의 File 조회
        List<Long> feedIds = allFeedQuery.getContent().stream().map(FeedProjection::getId).toList();
        List<FileProjection> fetchAllFeedImages = fileRepository.findAllInFileIdsWithFileType(feedIds, FileType.FEED);
        // 해시맵 으로 피드 ID별 이미지 묶기
        Map<Long, List<FileProjection>> imageMap = new HashMap<>();

        for (FileProjection file : fetchAllFeedImages) {
            imageMap
                    .computeIfAbsent(file.getFileId(), k -> new ArrayList<>())
                    .add(file);
        }
        List<FeedListResponse> feedResult = new ArrayList<>();
        for (FeedProjection feed : allFeedQuery.getContent()) {
            feedResult.add(FeedListResponse.from(
                    feed,
                    imageMap.getOrDefault(feed.getId(), Collections.emptyList())
            ));

        }
        return new SliceImpl<>(feedResult, allFeedQuery.getPageable(), allFeedQuery.hasNext());

    }

    @Transactional
    public FeedListResponse createFeed(Account account, List<MultipartFile> files, CreateFeedDto dto) {
        FeedEntity feedEntity = FeedEntity
                .builder()
                .contents(dto.getContent())
                .user(account)
                .viewCount(0)
                .build();
        FeedEntity savedEntity = feedRepository.save(feedEntity);
        if (files != null) {
            List<FileProjection> uploadFiles = fileService
                    .uploadAllFiles(files, account, savedEntity.getId(), FileType.FEED)
                    .stream()
                    .map(f -> new FileProjection() {
                        @Override
                        public Long getId() {
                            return f.getId();
                        }

                        @Override
                        public String getUrl() {
                            return f.getUrl();
                        }

                        @Override
                        public Long getFileId() {
                            return f.getFileId();
                        }

                        @Override
                        public FileType getFileType() {
                            return f.getFileType();
                        }
                    })
                    .collect(Collectors.toList());
            return FeedListResponse.from(feedEntity, uploadFiles);
        }
        return FeedListResponse.from(feedEntity, Collections.emptyList());
    }

    @Transactional(readOnly = true)
    public List<FeedDto> getHotFeed() {
        List<FeedDto> feedDtoList = new ArrayList<>();
        feedRepository.findTop4ByOrderByCreatedAtDesc().forEach(feed -> {
            feedDtoList.add(getQueryFeedDto(feed));
        });
        return feedDtoList;
    }

    public FeedEntity fetchFeedById(Long id) {
        return feedRepository.findById(id).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "404E00F00", "존재하지 않는 피드입니다.")
        );
    }


    @Transactional
    public FeedDto getQueryFeedDto(FeedEntity feed) {
        int commentCount = commentRepository.countByFeedId(feed.getId());
        int likeCount = likeRepository.countByFeedId(feed.getId());
        List<FileEntity> allFiles = fileService.getAllFiles(feed.getId(), FileType.FEED);
        return new FeedDto(
                feed,
                allFiles,
                commentCount,
                likeCount,
                false

        );

    }

    @Transactional
    public FeedDto getFeedById(Long id) {
        FeedEntity feedEntity = fetchFeedById(id);
        int commentCount = commentRepository.countByFeedId(id);
        int likeCount = likeRepository.countByFeedId(id);
        List<FileEntity> allFiles = fileService.getAllFiles(id, FileType.FEED);

        return new FeedDto(
                feedEntity,
                allFiles,
                commentCount,
                likeCount,
                false
        );
    }

    @Transactional
    public FeedDto getFeedByIdWithUser(Long id, Account account) {
        FeedEntity feedEntity = fetchFeedById(id);
        int commentCount = commentRepository.countByFeedId(id);
        int likeCount = likeRepository.countByFeedId(id);
        List<FileEntity> allFiles = fileService.getAllFiles(id, FileType.FEED);

        // 현재 로그인한 사용자의 좋아요 여부 확인
        boolean isLiked = false;
        if (account != null) {
            isLiked = likeRepository.existsByFeedIdAndAccountId(id, account.getId());
        }

        return new FeedDto(
                feedEntity,
                allFiles,
                commentCount,
                likeCount,
                isLiked
        );
    }

    public List<FeedDto> getTestAllFeed(Account account) {
        Long userId = null;
        if (account != null) {
            userId = account.getId();

        }
        List<FeedDto> feedDtoList = new ArrayList<>();

        feedRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).forEach(feed -> {
            feedDtoList.add(getFeedByIdWithUser(feed.getId(), account));
        });
        return feedDtoList;
    }

    @Transactional
    public LikeEntity likePost(Account user, Long feedId) {
        FeedEntity feedEntity = feedRepository.findById(feedId).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "400E00F001", "존재하지 않는 게시글입니다."));

        // 이미 좋아요를 눌렀으면 좋아요 취소
        if (likeRepository.existsByFeedIdAndAccountId(feedId, user.getId())) {
            likeRepository.deleteByFeedIdAndAccountId(feedId, user.getId());
            return null;
        }

        LikeEntity like = LikeEntity.builder()
                .account(user)
                .feed(feedEntity)
                .build();
        return likeRepository.save(like);
    }

    @Transactional
    public Boolean unLikePost(Account user, Long feedId) {
        int deleteCount = likeRepository.deleteByFeedIdAndAccountId(feedId, user.getId());
        log.info(deleteCount + " ");
        if (deleteCount == 1) {
            return true;
        }
        return false;
    }

    @Transactional
    public FeedDto updateFeed(Account account, Long feedId, List<MultipartFile> files, CreateFeedDto dto) {
        FeedEntity feedEntity = fetchFeedById(feedId);

        // 작성자 확인
        if (!feedEntity.getUser().getId().equals(account.getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "403E00F00", "수정 권한이 없습니다.");
        }

        // 내용 업데이트
        feedEntity.setContents(dto.getContent());

        // 기존 파일 삭제
        fileService.deleteFilesByFeedId(feedId);

        // 새 파일 저장
        if (files != null && !files.isEmpty()) {
            List<SaveFileDto> saveFileDtos = fileService.saveImage(files);
            List<FileEntity> saveFileEntities = fileService.saveFileEntities(saveFileDtos, feedId, account, FileType.FEED);
            return new FeedDto(
                    feedEntity,
                    saveFileEntities,
                    commentRepository.countByFeedId(feedId),
                    likeRepository.countByFeedId(feedId),
                    false
            );
        }

        return new FeedDto(
                feedEntity,
                null,
                commentRepository.countByFeedId(feedId),
                likeRepository.countByFeedId(feedId),
                false
        );
    }

    @Transactional
    public Boolean deleteFeed(Account account, Long feedId) {
        FeedEntity feedEntity = fetchFeedById(feedId);

        // 작성자 확인
        if (!feedEntity.getUser().getId().equals(account.getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "403E00F00", "삭제 권한이 없습니다.");
        }

        // 파일 삭제
        fileService.deleteFilesByFeedId(feedId);

        // 좋아요 삭제
        likeRepository.deleteByFeedId(feedId);

        // 댓글 삭제
        commentRepository.deleteByFeedId(feedId);

        // 피드 삭제
        feedRepository.deleteById(feedId);
        return true;
    }

    public CommentEntity createComent(Account user, Long feedId, CreateCommentDto dto) {
        FeedEntity feedEntity = feedRepository.findById(feedId).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "404E00F00", "존재하지 않는 피드입니다.")
        );
        CommentEntity comment = null;
        if (dto.getCommentId() != null) {
            comment = commentRepository.findById(dto.getCommentId()).orElseThrow(
                    () -> new ApiException(HttpStatus.NOT_FOUND, "404E00C00", "존재하지 않는 댓글입니다.")
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

    public List<CommentEntity> getAllRootComment(Long feedId) {
        return commentRepository.findAllByFeedIdAndReplyIsNull(feedId);
    }

    public List<CommentEntity> getAllCommentByReplyId(Long replyId) {
        return commentRepository.findAllByReplyId(replyId);
    }

    // FileService 관련 메서드 (FileService에도 추가 필요)
    // public void deleteFilesByFeedId(Long feedId) {
    //     fileService.deleteByFileId(feedId);
    // }
}