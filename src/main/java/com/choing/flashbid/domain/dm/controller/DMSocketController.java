package com.choing.flashbid.domain.dm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.choing.flashbid.domain.dm.dto.DMChatMessageDto;
import com.choing.flashbid.domain.dm.service.DMService;

@Controller
@RequiredArgsConstructor
public class DMSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final DMService dmService;

    /**
     * 클라이언트 → 서버: /app/dm.send/{roomId}
     * 1. (기존) 서버 → 구독자: /topic/dm/{roomId}
     * 2. (신규) 서버 → 수신자 개인: /topic/user/{receiverId}/dm
     * 3. (신규) 서버 → 발신자 개인: /topic/user/{senderId}/dm
     */
    @MessageMapping("/dm.send/{roomId}")
    public void sendDM(@Payload DMChatMessageDto messageDto) {
        System.out.println("🔥 받은 메시지: " + messageDto.getContents());

        // 1. 메시지를 DB에 저장
        DMChatMessageDto savedMessage = dmService.saveMessage(messageDto);

        // 2. (기존) 채팅방 토픽으로 메시지 전송 (채팅방 내부 실시간 대화용)
        messagingTemplate.convertAndSend("/topic/dm/" + messageDto.getRoomId(), savedMessage);

        // 3. (신규) 수신자와 발신자의 개인 알림 토픽으로 메시지 전송 (채팅 목록 실시간 갱신용)
        Long receiverId = savedMessage.getReceiverId();
        Long senderId = savedMessage.getSenderId();

        // 수신자에게 알림 전송
        messagingTemplate.convertAndSend("/topic/user/" + receiverId + "/dm", savedMessage);

        // 발신자에게도 알림을 보내 목록 순서를 갱신하도록 함
        if (!senderId.equals(receiverId)) { // 자신에게 보내는 메시지가 아닐 경우
            messagingTemplate.convertAndSend("/topic/user/" + senderId + "/dm", savedMessage);
        }
    }
}