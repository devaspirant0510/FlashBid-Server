package seoil.capstone.flashbid.domain.dm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoil.capstone.flashbid.domain.dm.entity.DMChat;
import seoil.capstone.flashbid.domain.dm.entity.DMChatRead;
import seoil.capstone.flashbid.domain.user.entity.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface DMChatReadRepository extends JpaRepository<DMChatRead, Long> {

    // 특정 메시지를 읽은 수
    long countByDmChat(DMChat dmChat);

    // 특정 사용자가 메시지를 읽었는지 확인
    Optional<DMChatRead> findByDmChatAndUser(DMChat dmChat, Account user);

    // 특정 채팅방의 미읽은 메시지 조회
    List<DMChatRead> findByDmChat_Room_IdAndUser(Long roomId, Account user);
}