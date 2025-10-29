package seoil.capstone.flashbid.domain.dm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seoil.capstone.flashbid.domain.dm.dto.DMChatResponseDto;
import seoil.capstone.flashbid.domain.dm.dto.DMRoomDetailDto;
import seoil.capstone.flashbid.domain.dm.dto.DMRoomListResponseDto;
import seoil.capstone.flashbid.domain.dm.entity.DMRoom;
import seoil.capstone.flashbid.domain.dm.service.DMService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.aop.annotation.AuthUser;
import seoil.capstone.flashbid.global.core.provider.JwtProvider;

import java.util.List;

@RestController
@RequestMapping("/api/dm")
@RequiredArgsConstructor
public class DMController {

    private final DMService dmService;
    private final AccountRepository accountRepository;
    private final JwtProvider jwtProvider; // JWT 토큰에서 userId 추출용

    @PostMapping("/create")
    public ResponseEntity<DMRoom> createDM(@RequestParam Long senderId,
                                           @RequestParam Long receiverId,
                                           @RequestParam String roomName) {

        Account sender = accountRepository.findById(senderId).orElseThrow();
        Account receiver = accountRepository.findById(receiverId).orElseThrow();

        DMRoom room = dmService.createDMRoom(sender, receiver, roomName);
        return ResponseEntity.ok(room);
    }

    /**
     * 채팅방의 메시지 내역 조회
     */
    @GetMapping("/chat/{roomId}")
    public ResponseEntity<List<DMChatResponseDto>> getDMChats(@PathVariable Long roomId) {
        List<DMChatResponseDto> chats = dmService.getChatsByRoomId(roomId);
        return ResponseEntity.ok(chats);
    }

    @AuthUser
    @GetMapping("/rooms")
    public ResponseEntity<List<DMRoomListResponseDto>> getUserDMRooms(Account account) {
        List<DMRoomListResponseDto> rooms = dmService.getUserDMRooms(account.getId());
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<DMRoomDetailDto> getDMRoomDetail(@PathVariable Long roomId) {
        DMRoomDetailDto roomDetail = dmService.getDMRoomDetail(roomId);
        return ResponseEntity.ok(roomDetail);
    }

    @AuthUser
    @PostMapping("/chat/read/{roomId}")
    public ResponseEntity<Void> markMessagesAsRead(
            @PathVariable Long roomId,
            Account account) {
        dmService.markMessagesAsRead(roomId, account.getId());
        return ResponseEntity.ok().build();
    }
}