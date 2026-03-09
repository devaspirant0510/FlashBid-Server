package com.choing.flashbid.domain.inquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.choing.flashbid.domain.inquiry.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}