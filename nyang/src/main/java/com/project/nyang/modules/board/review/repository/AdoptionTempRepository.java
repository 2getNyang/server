package com.project.nyang.modules.board.review.repository;

import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 임시 AdoptionRepository
 *
 * @author : 박세정
 * @fileName : AdoptionRepository
 * @since : 2025-07-10
 */
public interface AdoptionTempRepository extends JpaRepository<PetApplicationForm, Long> {
}
