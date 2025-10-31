package seoil.capstone.flashbid.domain.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    /**
     * 공지사항 목록 조회 (페이징 + 정렬)
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param sortDir 정렬 방향 ("ASC" or "DESC")
     * @return Page<NoticeDTO>
     */
    public Page<NoticeDTO> getNoticesWithPaging(int page, int size, String sortDir) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
        return noticeRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * 전체 공지사항 목록 조회
     * @return List<NoticeDTO>
     */
    public List<NoticeDTO> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 단일 공지사항 조회
     * @param id 공지사항 ID
     * @return NoticeDTO
     */
    public NoticeDTO getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));

        // 조회수 1 증가
        notice.setViewCount(notice.getViewCount() + 1);
        noticeRepository.save(notice);

        return convertToDTO(notice);
    }


    /**
     * 공지사항 생성
     * @param dto NoticeDTO
     * @return 생성된 NoticeDTO
     */
    public NoticeDTO createNotice(NoticeDTO dto) {
        Notice notice = Notice.builder()
                .category(dto.getCategory())
                .title(dto.getTitle())
                .content(dto.getContent())
                .viewCount(0)
                .build();
        Notice saved = noticeRepository.save(notice);
        return convertToDTO(saved);
    }

    /**
     * 공지사항 수정
     * @param id 공지사항 ID
     * @param dto 수정할 데이터
     * @return 수정된 NoticeDTO
     */
    public NoticeDTO updateNotice(Long id, NoticeDTO dto) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));

        notice.setCategory(dto.getCategory());
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());

        Notice updated = noticeRepository.save(notice);
        return convertToDTO(updated);
    }

    /**
     * 공지사항 삭제
     * @param id 공지사항 ID
     */
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    /**
     * Entity → DTO 변환 메서드
     */
    private NoticeDTO convertToDTO(Notice notice) {
        return NoticeDTO.builder()
                .id(notice.getId())
                .category(notice.getCategory())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .viewCount(notice.getViewCount())
                .build();
    }
}
