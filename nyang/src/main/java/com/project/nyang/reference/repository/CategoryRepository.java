package com.project.nyang.reference.repository;

import com.project.nyang.reference.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 카테고리 관련 Repository
 *
 * @author : 박세정
 * @fileName : CategoryRepository
 * @since : 2025-07-09
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
