package seoil.capstone.flashbid.domain.dm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seoil.capstone.flashbid.domain.dm.entity.DMRoom;

import java.util.List;

public interface DMRoomRepository extends JpaRepository<DMRoom, Long> {

    @Query("""
    SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
    FROM DMRoom r JOIN r.participants p1 JOIN r.participants p2
    WHERE (p1.participant.id = :userId1 AND p2.participant.id = :userId2)
       OR (p1.participant.id = :userId2 AND p2.participant.id = :userId1)
    """)
    boolean existsByParticipants(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    /**
     * 사용자가 참여한 모든 채팅방을 최신 메시지 기준으로 정렬하여 반환
     */
    @Query(value = """
    SELECT DISTINCT ON (r.id)
      r.*
    FROM dm_room r
    JOIN dm_participate p ON r.id = p.room_id AND p.participate = :userId
    LEFT JOIN dm_chat c ON r.id = c.room_id
    ORDER BY r.id, c.created_at DESC NULLS LAST, r.created_at DESC
    """, nativeQuery = true)
    List<DMRoom> findRoomsByUserIdOrderByLatestMessage(@Param("userId") Long userId);
}
