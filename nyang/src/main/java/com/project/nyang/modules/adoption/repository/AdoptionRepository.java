package com.project.nyang.modules.adoption.repository;

import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AdoptionRepository 입니다
 *
 * @author : 이지은
 * @fileName : AdoptionRepository
 * @since : 25. 7. 14.
 */
public interface AdoptionRepository extends JpaRepository<PetApplicationForm, Long> {
    boolean existsByUserIdAndAnimal_DesertionNo(Long userId, String desertionNo);
}
