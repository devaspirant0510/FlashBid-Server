package seoil.capstone.flashbid.domain.dm.dto;

import lombok.*;
import seoil.capstone.flashbid.domain.dm.entity.DMChat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DMChatResponseDto {
    private Long id;
    private Long roomId;
    private Long senderId;
    private Long receiverId;
    private String contents;
    private String dmType;
    private LocalDateTime createdAt;
    private long readCount;

    // DMChat 엔티티를 DTO로 변환하는 정적 팩토리 메서드
    public static DMChatResponseDto from(DMChat chat) {
        return DMChatResponseDto.builder()
                .id(chat.getId())
                .roomId(chat.getRoom().getId())
                .senderId(chat.getSender().getId())
                .receiverId(chat.getReceiver().getId())
                .contents(chat.getContents())
                .dmType(chat.getDmType().toString())
                .createdAt(chat.getCreatedAt())
                .readCount(0)  // 기본값
                .build();
    }

    // ✅ readCount를 포함한 새로운 메서드
    public static DMChatResponseDto fromWithReadCount(DMChat chat, long readCount) {
        return DMChatResponseDto.builder()
                .id(chat.getId())
                .roomId(chat.getRoom().getId())
                .senderId(chat.getSender().getId())
                .receiverId(chat.getReceiver().getId())
                .contents(chat.getContents())
                .dmType(chat.getDmType().toString())
                .createdAt(chat.getCreatedAt())
                .readCount(readCount)  // ✅ 읽은 수 포함
                .build();
    }
}