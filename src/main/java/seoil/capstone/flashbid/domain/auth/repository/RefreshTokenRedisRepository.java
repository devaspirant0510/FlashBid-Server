package seoil.capstone.flashbid.domain.auth.repository;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<String,String> {
}
