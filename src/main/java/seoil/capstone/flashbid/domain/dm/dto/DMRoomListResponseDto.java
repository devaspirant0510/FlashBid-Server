package seoil.capstone.flashbid.domain.dm.dto;

import lombok.*;
import seoil.capstone.flashbid.domain.dm.entity.DMChat;
import seoil.capstone.flashbid.domain.dm.entity.DMRoom;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DMRoomListResponseDto {
    private Long roomId;
    private String roomName;
    private Long participantId;
    private String participantNickname;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private long unreadCount;  // ✅ 추가

    // 기존 메서드 (unreadCount 없이)
    public static DMRoomListResponseDto from(DMRoom room, Long currentUserId) {
        // 현재 사용자가 아닌 다른 참가자 찾기
        Long participantId = room.getParticipants().stream()
                .map(p -> p.getParticipant().getId())
                .filter(id -> !id.equals(currentUserId))
                .findFirst()
                .orElse(null);

        String participantNickname = room.getParticipants().stream()
                .filter(p -> !p.getParticipant().getId().equals(currentUserId))
                .map(p -> p.getParticipant().getNickname())
                .findFirst()
                .orElse("Unknown");

        // 가장 최신 메시지 가져오기
        String lastMessage = "";
        LocalDateTime lastMessageTime = room.getUpdatedAt();

        if (!room.getChats().isEmpty()) {
            DMChat lastChat = room.getChats().get(room.getChats().size() - 1);
            lastMessage = lastChat.getContents();
            lastMessageTime = lastChat.getCreatedAt();
        }

        return DMRoomListResponseDto.builder()
                .roomId(room.getId())
                .roomName(room.getRoomName())
                .participantId(participantId)
                .participantNickname(participantNickname)
                .lastMessage(lastMessage)
                .lastMessageTime(lastMessageTime)
                .unreadCount(0)  // 기본값
                .build();
    }

    // ✅ unreadCount를 포함한 새로운 메서드
    public static DMRoomListResponseDto fromWithUnreadCount(DMRoom room, Long currentUserId, long unreadCount) {
        // 현재 사용자가 아닌 다른 참가자 찾기
        Long participantId = room.getParticipants().stream()
                .map(p -> p.getParticipant().getId())
                .filter(id -> !id.equals(currentUserId))
                .findFirst()
                .orElse(null);

        String participantNickname = room.getParticipants().stream()
                .filter(p -> !p.getParticipant().getId().equals(currentUserId))
                .map(p -> p.getParticipant().getNickname())
                .findFirst()
                .orElse("Unknown");

        // 가장 최신 메시지 가져오기
        String lastMessage = "";
        LocalDateTime lastMessageTime = room.getUpdatedAt();

        if (!room.getChats().isEmpty()) {
            DMChat lastChat = room.getChats().get(room.getChats().size() - 1);
            lastMessage = lastChat.getContents();
            lastMessageTime = lastChat.getCreatedAt();
        }

        return DMRoomListResponseDto.builder()
                .roomId(room.getId())
                .roomName(room.getRoomName())
                .participantId(participantId)
                .participantNickname(participantNickname)
                .lastMessage(lastMessage)
                .lastMessageTime(lastMessageTime)
                .unreadCount(unreadCount)  // ✅ 미읽은 수 포함
                .build();
    }
}