package seoil.capstone.flashbid.domain.inquiry.dto;

import lombok.*;
import seoil.capstone.flashbid.domain.inquiry.entity.Reply;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ReplyDto {
    private Long id;
    private String content;
    private String createdAt;
    private Long adminId;

    public static ReplyDto from(Reply reply) {
        if (reply == null) return null;
        return ReplyDto.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .createdAt(reply.getCreatedAt() != null ? reply.getCreatedAt().toString() : null)
                .adminId(reply.getUser() != null ? reply.getUser().getId() : null)
                .build();
    }
}