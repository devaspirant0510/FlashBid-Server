package seoil.capstone.flashbid.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.user.entity.FollowEntity;

import java.util.List;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    List<FollowEntity> findAllByFollowerId(Long id);

    List<FollowEntity> findAllByFollowingId(Long id);

    int deleteByFollowerIdAndFollowingId(Long followerId,Long followingId);

    int countByFollowerId(Long id);

    int countByFollowingId(Long id);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
