package seoil.capstone.flashbid.domain.feed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.feed.entity.FeedEntity;

public interface FeedRepository extends JpaRepository<FeedEntity,Long> {

}
