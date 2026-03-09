package com.choing.flashbid.domain.inquiry.dto;

import lombok.*;
import com.choing.flashbid.domain.inquiry.entity.InquiryType;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class InquiryUpdateRequest {
    private String title;           // null이면 변경 없음
    private String content;         // null이면 변경 없음
    private InquiryType inquiryType; // null이면 변경 없음
}