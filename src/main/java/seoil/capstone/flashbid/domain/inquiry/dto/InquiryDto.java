package seoil.capstone.flashbid.domain.inquiry.dto;

import lombok.*;
import seoil.capstone.flashbid.domain.inquiry.entity.Inquiry;
import seoil.capstone.flashbid.domain.inquiry.entity.InquiryType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryDto {
    private Long id;
    private String title;
    private String content;
    private InquiryType inquiryType;
    private boolean replyStatus;
    private String createdAt;

    private ReplyDto reply;

    public static InquiryDto from(Inquiry inquiry) {
        return InquiryDto.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .inquiryType(inquiry.getInquiryType())
                .replyStatus(inquiry.getReply() != null)
                .createdAt(inquiry.getCreatedAt() != null ? inquiry.getCreatedAt().toString() : null)
                .reply(ReplyDto.from(inquiry.getReply()))
                .build();
    }
}