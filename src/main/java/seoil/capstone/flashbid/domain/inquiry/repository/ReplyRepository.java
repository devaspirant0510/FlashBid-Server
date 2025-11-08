package seoil.capstone.flashbid.domain.inquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.inquiry.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}