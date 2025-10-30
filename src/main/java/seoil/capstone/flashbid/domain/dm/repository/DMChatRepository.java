package seoil.capstone.flashbid.domain.dm.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import seoil.capstone.flashbid.domain.dm.entity.DMChat;

import java.util.List;

@Repository
public interface DMChatRepository extends JpaRepository<DMChat, Long> {
    List<DMChat> findByRoomIdOrderByCreatedAtAsc(Long roomId);

    @Query("SELECT d FROM DMChat d WHERE d.room.id = :roomId " +
            "AND d.receiver.id = :userId " +
            "AND NOT EXISTS (SELECT 1 FROM DMChatRead dcr WHERE dcr.dmChat = d AND dcr.user.id = :userId)")
    List<DMChat> findUnreadMessages(@Param("roomId") Long roomId, @Param("userId") Long userId);
}