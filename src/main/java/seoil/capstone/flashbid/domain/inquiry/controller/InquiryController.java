package seoil.capstone.flashbid.domain.inquiry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seoil.capstone.flashbid.domain.inquiry.dto.InquiryDto;
import seoil.capstone.flashbid.domain.inquiry.dto.InquiryRequest;
import seoil.capstone.flashbid.domain.inquiry.dto.InquiryUpdateRequest;
import seoil.capstone.flashbid.domain.inquiry.service.InquiryService;
import seoil.capstone.flashbid.global.common.response.ApiResult;
import seoil.capstone.flashbid.global.core.provider.JwtProvider;
import io.jsonwebtoken.Claims;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;
    private final JwtProvider jwtProvider;

    private Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Claims claims = jwtProvider.parseClaims(token);
        return claims.get("id", Long.class);
    }

    private boolean isAdmin(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Claims claims = jwtProvider.parseClaims(token);
        return "ADMIN".equals(claims.get("role", String.class));
    }

    @PostMapping
    public ResponseEntity<ApiResult<InquiryDto>> create(@RequestBody InquiryRequest request, HttpServletRequest req) {
        Long userId = getUserId(req);
        InquiryDto dto = inquiryService.createInquiry(userId, request);
        return ResponseEntity.ok(ApiResult.created(dto, "문의 등록 완료"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResult<List<InquiryDto>>> myInquiries(HttpServletRequest req) {
        Long userId = getUserId(req);
        return ResponseEntity.ok(ApiResult.ok(inquiryService.getMyInquiries(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<InquiryDto>> getInquiry(@PathVariable Long id, HttpServletRequest req) {
        Long userId = getUserId(req);
        boolean isAdmin = isAdmin(req);
        return ResponseEntity.ok(ApiResult.ok(inquiryService.getInquiry(userId, id, isAdmin)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResult<InquiryDto>> updateInquiry(
            @PathVariable Long id,
            @RequestBody InquiryUpdateRequest request,
            HttpServletRequest req
    ) {
        Long userId = getUserId(req);
        boolean isAdmin = isAdmin(req);
        InquiryDto dto = inquiryService.updateInquiry(userId, isAdmin, id, request);
        return ResponseEntity.ok(ApiResult.ok(dto, "문의 수정 완료"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteInquiry(@PathVariable Long id, HttpServletRequest req) {
        Long userId = getUserId(req);
        boolean isAdmin = isAdmin(req);
        inquiryService.deleteInquiry(userId, id, isAdmin);
        return ResponseEntity.ok(ApiResult.ok(null, "문의 삭제 완료"));
    }
}