package com.project.nyang.modules.adoption.repository;

import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * AdoptionRepository 입니다
 *
 * @author : 이지은
 * @fileName : AdoptionRepository
 * @since : 25. 7. 14.
 */
public interface AdoptionRepository extends JpaRepository<PetApplicationForm, Long> {
    boolean existsByUserIdAndAnimal_DesertionNo(Long userId, String desertionNo);

    @EntityGraph(attributePaths = {"animal"})
    Page<PetApplicationForm> findByUser_Id(Long userId, Pageable pageable);


    //입양신청게시글 작성폼에서 사용. 사용자의 모든 입양신청내역 불러오는 쿼리
    @Query("SELECT p FROM PetApplicationForm p " +
            "JOIN FETCH p.animal " +
            "JOIN FETCH p.shelter sh " +
            "JOIN FETCH sh.region " +
            "JOIN FETCH sh.subRegion " +
            "WHERE p.user.id = :userId " +
            "ORDER BY p.formCreatedAt ASC")
    List<PetApplicationForm> findAllByUser_IdOrderByFormCreatedAtAsc(Long userId);

}
