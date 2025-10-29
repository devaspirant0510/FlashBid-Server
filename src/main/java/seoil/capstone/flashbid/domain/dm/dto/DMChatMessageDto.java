package seoil.capstone.flashbid.domain.dm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import seoil.capstone.flashbid.domain.dm.entity.DMChat;

@Getter
@NoArgsConstructor
public class DMChatMessageDto {
    private Long roomId;
    private Long senderId;
    private Long receiverId;
    private String contents;
    private DMChat.DMType dmType;
    private String createdAt; // ✅ 1. 이 필드를 추가하세요.

    @Builder
    public DMChatMessageDto(Long roomId, Long senderId, Long receiverId, String contents, DMChat.DMType dmType, String createdAt) { // ✅ 2. 생성자에도 추가
        this.roomId = roomId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.contents = contents;
        this.dmType = dmType;
        this.createdAt = createdAt; // ✅ 3. this.createdAt 추가
    }
}