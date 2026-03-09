package com.choing.flashbid.domain.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.choing.flashbid.domain.inquiry.dto.ReplyRequest;
import com.choing.flashbid.domain.inquiry.entity.Inquiry;
import com.choing.flashbid.domain.inquiry.entity.Reply;
import com.choing.flashbid.domain.inquiry.repository.InquiryRepository;
import com.choing.flashbid.domain.inquiry.repository.ReplyRepository;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.domain.user.repository.AccountRepository;
import com.choing.flashbid.global.common.error.ApiException;
import com.choing.flashbid.global.common.enums.UserType;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final InquiryRepository inquiryRepository;
    private final ReplyRepository replyRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Reply updateReply(Long adminId, Long inquiryId, ReplyRequest request) {
        Account admin = accountRepository.findById(adminId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Admin Not Found", "존재하지 않는 관리자입니다."));
        if (admin.getUserType() != UserType.ADMIN) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Access Denied", "관리자만 답변을 수정할 수 있습니다.");
        }

        Inquiry inquiry = inquiryRepository.findByIdWithReply(inquiryId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Inquiry Not Found", "문의글을 찾을 수 없습니다."));

        if (inquiry.getReply() == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Reply Not Found", "해당 문의에는 아직 답변이 없습니다.");
        }

        inquiry.getReply().setContent(request.getContent());
        return inquiry.getReply();
    }

    @Transactional
    public Reply createReply(Long adminId, Long inquiryId, ReplyRequest request) {
        Account admin = accountRepository.findById(adminId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Admin Not Found", "존재하지 않는 관리자입니다."));
        if (admin.getUserType() != UserType.ADMIN) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Access Denied", "관리자만 답변을 작성할 수 있습니다.");
        }

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Inquiry Not Found", "문의글을 찾을 수 없습니다."));

        if (inquiry.isReplyStatus()) {
            throw new ApiException(HttpStatus.CONFLICT, "Already Replied", "이미 답변이 등록된 문의입니다.");
        }


        Reply reply = Reply.builder()
                .user(admin)
                .content(request.getContent())
                .build();

        inquiry.linkReply(replyRepository.save(reply));
        return reply;
    }
}