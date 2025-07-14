package com.project.nyang.modules.mypage.repository;

import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 임시 입양 신청 repository -> 삭제 예정
 *
 * @author : 박세정
 * @fileName : TempPetApplicationFormRepository
 * @since : 2025-07-12
 */
public interface TempPetApplicationFormRepository extends JpaRepository<PetApplicationForm, Long> {
}