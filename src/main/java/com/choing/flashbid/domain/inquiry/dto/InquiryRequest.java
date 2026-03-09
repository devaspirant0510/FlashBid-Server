package com.choing.flashbid.domain.inquiry.dto;

import lombok.*;
import com.choing.flashbid.domain.inquiry.entity.InquiryType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryRequest {
    private String title;
    private String content;
    private InquiryType inquiryType;
}