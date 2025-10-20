package seoil.capstone.flashbid.domain.dm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.repository.AuctionRepository;
import seoil.capstone.flashbid.domain.dm.dto.DMChatMessageDto;
import seoil.capstone.flashbid.domain.dm.dto.DMChatResponseDto;
import seoil.capstone.flashbid.domain.dm.dto.DMRoomDetailDto;
import seoil.capstone.flashbid.domain.dm.dto.DMRoomListResponseDto;
import seoil.capstone.flashbid.domain.dm.entity.*;
import seoil.capstone.flashbid.domain.dm.repository.*;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.file.repository.FileRepository;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.enums.FileType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DMService {

    private final DMRoomRepository dmRoomRepository;
    private final DMParticipateRepository dmParticipateRepository;
    private final DMChatRepository dmChatRepository;
    private final DMChatReadRepository dmChatReadRepository;  // ✅ 추가
    private final AccountRepository accountRepository;
    private final AuctionRepository auctionRepository;
    private final FileRepository fileRepository;

    /**
     * 판매자-구매자, 혹은 일반 사용자 간 DM방 생성
     */
    @Transactional
    public DMRoom createDMRoom(Account sender, Account receiver, String roomName) {
        boolean exists = dmRoomRepository.existsByParticipants(sender.getId(), receiver.getId());
        if (exists) {
            throw new IllegalStateException("이미 존재하는 DM방입니다.");
        }

        DMRoom room = DMRoom.builder()
                .roomName(roomName)
                .owner(sender)
                .build();
        DMRoom savedRoom = dmRoomRepository.save(room);

        DMParticipate p1 = DMParticipate.builder().room(savedRoom).participant(sender).build();
        DMParticipate p2 = DMParticipate.builder().room(savedRoom).participant(receiver).build();
        dmParticipateRepository.saveAll(List.of(p1, p2));

        return savedRoom;
    }

    /**
     * 경매 정보와 함께 DM방 생성 (경매 확정 시 호출)
     */
    @Transactional
    public DMRoom createDMRoomWithAuctionInfo(Account sender, Account receiver, String roomName, Long auctionId) {
        // 경매 정보 조회
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid auctionId"));

        // 중복 확인
        boolean exists = dmRoomRepository.existsByParticipants(sender.getId(), receiver.getId());
        if (exists) {
            throw new IllegalStateException("이미 존재하는 DM방입니다.");
        }

        // 채팅방 생성 (auction 정보 포함)
        DMRoom room = DMRoom.builder()
                .roomName(roomName)
                .owner(sender)
                .auction(auction)
                .build();
        DMRoom savedRoom = dmRoomRepository.save(room);

        // 참가자 추가
        DMParticipate p1 = DMParticipate.builder().room(savedRoom).participant(sender).build();
        DMParticipate p2 = DMParticipate.builder().room(savedRoom).participant(receiver).build();
        dmParticipateRepository.saveAll(List.of(p1, p2));

        // 초기 메시지: 경매 정보 마커
        String auctionInfoMessage = String.format("AUCTION_INFO:%d", auctionId);
        DMChat auctionChat = DMChat.builder()
                .sender(sender)
                .receiver(receiver)
                .room(savedRoom)
                .contents(auctionInfoMessage)
                .dmType(DMChat.DMType.MESSAGE)
                .createdAt(LocalDateTime.now())
                .build();

        dmChatRepository.save(auctionChat);

        return savedRoom;
    }

    @Transactional
    public DMChatMessageDto saveMessage(DMChatMessageDto dto) {
        var sender = accountRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid senderId"));
        var receiver = accountRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid receiverId"));
        var room = dmRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid roomId"));

        LocalDateTime now = LocalDateTime.now(); // ✅ 2. 현재 시간을 변수로 저장

        DMChat dmChat = DMChat.builder()
                .sender(sender)
                .receiver(receiver)
                .room(room)
                .contents(dto.getContents())
                .dmType(dto.getDmType())
                .createdAt(now) // ✅ 3. 저장 시 변수 사용
                .build();

        dmChatRepository.save(dmChat);

        // ✅ 4. 반환하는 DTO에 createdAt 값을 문자열로 변환하여 추가
        return DMChatMessageDto.builder()
                .roomId(room.getId())
                .senderId(sender.getId())
                .receiverId(receiver.getId())
                .contents(dto.getContents())
                .dmType(dto.getDmType())
                .createdAt(now.toString()) // ✅ 5. 이 라인 추가
                .build();
    }

    /**
     * 채팅방의 메시지 내역을 DTO로 변환하여 반환 (✅ readCount 포함)
     */
    @Transactional(readOnly = true)
    public List<DMChatResponseDto> getChatsByRoomId(Long roomId) {
        List<DMChat> chats = dmChatRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
        return chats.stream()
                .map(chat -> {
                    long readCount = dmChatReadRepository.countByDmChat(chat);
                    return DMChatResponseDto.fromWithReadCount(chat, readCount);
                })
                .collect(Collectors.toList());
    }

    /**
     * 현재 사용자의 모든 DM 채팅방 목록을 최신 메시지 기준으로 조회 (✅ unreadCount 포함)
     */
    @Transactional(readOnly = true)
    public List<DMRoomListResponseDto> getUserDMRooms(Long userId) {
        List<DMRoom> rooms = dmRoomRepository.findRoomsByUserIdOrderByLatestMessage(userId);

        return rooms.stream()
                .map(room -> {
                    long unreadCount = dmChatRepository.findUnreadMessages(room.getId(), userId).size();

                    return DMRoomListResponseDto.fromWithUnreadCount(room, userId, unreadCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DMRoomDetailDto getDMRoomDetail(Long roomId) {
        DMRoom room = dmRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다"));

        if (room.getAuction() == null) {
            return DMRoomDetailDto.builder()
                    .roomId(room.getId())
                    .roomName(room.getRoomName())
                    .auction(null)
                    .build();
        }

        Auction auction = room.getAuction();
        String imageUrl = "";

        // 상품 이미지 조회 (GOODS 타입의 파일 중 첫 번째)
        if (auction.getGoods() != null) {
            List<FileEntity> files = fileRepository.findAllByFileIdAndFileType(
                    auction.getGoods().getId(),
                    FileType.GOODS
            );
            if (!files.isEmpty()) {
                String url = files.get(0).getUrl();
                // ✅ 상대 경로인 경우 전체 URL로 변환
                if (url.startsWith("/uploads")) {
                    // application.properties의 서버 URL 가져오기
                    String serverUrl = System.getProperty("server.url", "http://localhost:8080");
                    imageUrl = serverUrl + url;
                } else {
                    imageUrl = url;
                }
            }
        }

        DMRoomDetailDto.AuctionDetailDto auctionDto = DMRoomDetailDto.AuctionDetailDto.builder()
                .id(auction.getId())
                .goodsTitle(auction.getGoods() != null ? auction.getGoods().getTitle() : "")
                .startPrice(auction.getStartPrice())
                .endTime(auction.getEndTime() != null ? auction.getEndTime().toString() : "")
                .imageUrl(imageUrl)
                .build();

        return DMRoomDetailDto.builder()
                .roomId(room.getId())
                .roomName(room.getRoomName())
                .auction(auctionDto)
                .build();
    }

    /**
     * ✅ 메시지를 읽음 처리
     */
    @Transactional
    public void markMessagesAsRead(Long roomId, Long userId) {
        DMRoom room = dmRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다"));

        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        // 미읽은 메시지 조회
        List<DMChat> unreadChats = dmChatRepository.findUnreadMessages(roomId, userId);

        unreadChats.forEach(chat -> {
            // 이미 읽음 기록이 있는지 확인
            if (dmChatReadRepository.findByDmChatAndUser(chat, user).isEmpty()) {
                DMChatRead read = DMChatRead.builder()
                        .dmChat(chat)
                        .user(user)
                        .build();
                dmChatReadRepository.save(read);
            }
        });
    }
}