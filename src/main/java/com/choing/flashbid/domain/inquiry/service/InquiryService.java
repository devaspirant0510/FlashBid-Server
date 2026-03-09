// src/main/java/seoil/capstone/flashbid/domain/customer/service/InquiryService.java
package com.choing.flashbid.domain.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.choing.flashbid.domain.inquiry.dto.InquiryDto;
import com.choing.flashbid.domain.inquiry.dto.InquiryRequest;
import com.choing.flashbid.domain.inquiry.dto.InquiryUpdateRequest;
import com.choing.flashbid.domain.inquiry.entity.Inquiry;
import com.choing.flashbid.domain.inquiry.repository.InquiryRepository;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.domain.user.repository.AccountRepository;
import com.choing.flashbid.global.common.error.ApiException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public InquiryDto createInquiry(Long userId, InquiryRequest request) {
        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User Not Found", "존재하지 않는 유저입니다."));
        Inquiry inquiry = Inquiry.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .inquiryType(request.getInquiryType())
                .build();
        return InquiryDto.from(inquiryRepository.save(inquiry));
    }

    public List<InquiryDto> getMyInquiries(Long userId) {
        return inquiryRepository.findAllByUserIdWithReply(userId)
                .stream().map(InquiryDto::from).toList();
    }

    public InquiryDto getInquiry(Long userId, Long id, boolean isAdmin) {
        Inquiry inquiry = inquiryRepository.findByIdWithReply(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Inquiry Not Found", "문의글을 찾을 수 없습니다."));
        if (!isAdmin && !inquiry.isOwnedBy(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Access Denied", "해당 문의글은 본인만 열람 가능합니다.");
        }
        return InquiryDto.from(inquiry);
    }

    @Transactional
    public InquiryDto updateInquiry(Long currentUserId, boolean isAdmin, Long inquiryId, InquiryUpdateRequest req) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Inquiry Not Found", "문의글을 찾을 수 없습니다."));

        if (!isAdmin && !inquiry.isOwnedBy(currentUserId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Access Denied", "해당 문의글은 본인만 수정 가능합니다.");
        }

        if (req.getTitle() != null)   inquiry.setTitle(req.getTitle());
        if (req.getContent() != null) inquiry.setContent(req.getContent());
        if (req.getInquiryType() != null) inquiry.setInquiryType(req.getInquiryType());

        return InquiryDto.from(inquiry);
    }

    @Transactional
    public void deleteInquiry(Long userId, Long id, boolean isAdmin) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Inquiry Not Found", "문의글을 찾을 수 없습니다."));
        if (!isAdmin && !inquiry.isOwnedBy(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Access Denied", "삭제 권한이 없습니다.");
        }
        inquiryRepository.delete(inquiry);
    }

    public List<InquiryDto> getAll() {
        return inquiryRepository.findAllWithReply()
                .stream().map(InquiryDto::from).toList();
    }
}