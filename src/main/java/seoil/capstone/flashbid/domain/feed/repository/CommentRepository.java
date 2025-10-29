package seoil.capstone.flashbid.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.feed.entity.CommentEntity;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    int countByFeedId(Long feedId);
    List<CommentEntity> findAllByFeedId(Long feedId);
    List<CommentEntity> findAllByReplyId(Long replyId);
    List<CommentEntity> findAllByFeedIdAndReplyIsNull(Long feedId);
    void deleteByFeedId(Long feedId);
}
