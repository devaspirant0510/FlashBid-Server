package com.choing.flashbid.domain.inquiry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.choing.flashbid.domain.inquiry.dto.InquiryDto;
import com.choing.flashbid.domain.inquiry.dto.ReplyRequest;
import com.choing.flashbid.domain.inquiry.entity.Reply;
import com.choing.flashbid.domain.inquiry.service.ReplyService;
import com.choing.flashbid.domain.inquiry.service.InquiryService;
import com.choing.flashbid.global.common.response.ApiResult;
import com.choing.flashbid.global.core.provider.JwtProvider;
import io.jsonwebtoken.Claims;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/inquiries")
public class AdminInquiryController {

    private final ReplyService replyService;
    private final JwtProvider jwtProvider;
    private final InquiryService inquiryService;

    private Long getAdminId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Claims claims = jwtProvider.parseClaims(token);
        return claims.get("id", Long.class);
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<ApiResult<Reply>> createReply(@PathVariable Long id, @RequestBody ReplyRequest request, HttpServletRequest req) {
        Long adminId = getAdminId(req);
        Reply reply = replyService.createReply(adminId, id, request);
        return ResponseEntity.ok(ApiResult.created(reply, "답변 등록 완료"));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/reply")
    public ResponseEntity<ApiResult<Reply>> updateReply(
            @PathVariable Long id,
            @RequestBody ReplyRequest request,
            HttpServletRequest req
    ) {
        Long adminId = getAdminId(req);
        Reply reply = replyService.updateReply(adminId, id, request);
        return ResponseEntity.ok(ApiResult.ok(reply, "답변 수정 완료"));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResult<List<InquiryDto>>> listAll() {
        return ResponseEntity.ok(ApiResult.ok(inquiryService.getAll()));
    }
}