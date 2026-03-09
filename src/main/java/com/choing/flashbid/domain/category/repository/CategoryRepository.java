package com.choing.flashbid.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.choing.flashbid.domain.category.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
    List<CategoryEntity> findAllByRootIdIsNull();
    Optional<CategoryEntity> findByName(String name);
}
