package seoil.capstone.flashbid.domain.inquiry.dto;

import lombok.*;
import seoil.capstone.flashbid.domain.inquiry.entity.InquiryType;

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