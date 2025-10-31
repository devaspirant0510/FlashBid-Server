package seoil.capstone.flashbid.domain.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final NoticeService noticeService;

    // 전체 공지 조회
    @GetMapping
    // 올바른 경우
    public ResponseEntity<List<NoticeDTO>> getAllNotices() {
        List<NoticeDTO> list = noticeService.getAllNotices();
        return ResponseEntity.ok(list);
    }


    // 페이징 조회
    @GetMapping("/paged")
    public ResponseEntity<Page<NoticeDTO>> getNoticesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Page<NoticeDTO> pagedResult = noticeService.getNoticesWithPaging(page, size, sortDir);
        return ResponseEntity.ok(pagedResult);
    }

    // 단일 공지 조회
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDTO> getNotice(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getNoticeById(id));
    }

    // 공지 생성
    @PostMapping
    public ResponseEntity<NoticeDTO> createNotice(@RequestBody NoticeDTO dto) {
        return ResponseEntity.ok(noticeService.createNotice(dto));
    }

    // 공지 수정
    @PutMapping("/{id}")
    public ResponseEntity<NoticeDTO> updateNotice(@PathVariable Long id, @RequestBody NoticeDTO dto) {
        return ResponseEntity.ok(noticeService.updateNotice(id, dto));
    }

    // 공지 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }
}
