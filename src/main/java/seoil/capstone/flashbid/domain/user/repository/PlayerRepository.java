package seoil.capstone.flashbid.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.user.entity.Player;


public interface PlayerRepository extends JpaRepository<Player,Long> {
}
