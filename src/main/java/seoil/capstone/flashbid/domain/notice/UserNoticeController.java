package seoil.capstone.flashbid.domain.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/api/user/notices")
@RequiredArgsConstructor
public class UserNoticeController {

    private final NoticeService noticeService;

    // 전체 공지 조회
    @GetMapping
    public List<NoticeDTO> getAllNotices() {
        return noticeService.getAllNotices();
    }

    // 공지 상세 조회
    @GetMapping("/{id}")
    public NoticeDTO getNotice(@PathVariable Long id) {
        return noticeService.getNoticeById(id);
    }

    // 공지 페이징 조회
    @GetMapping("/paged")
    public PagedResponse<NoticeDTO> getNoticesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Page<NoticeDTO> noticePage = noticeService.getNoticesWithPaging(page, size, sortDir);
        return new PagedResponse<>(
                noticePage.getContent(),
                noticePage.getNumber(),
                noticePage.getSize(),
                noticePage.getTotalElements(),
                noticePage.getTotalPages(),
                noticePage.isLast()
        );
    }
}
