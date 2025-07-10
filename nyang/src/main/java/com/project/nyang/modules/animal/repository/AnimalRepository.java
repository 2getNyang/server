package com.project.nyang.modules.animal.repository;

import com.project.nyang.modules.animal.dto.AnimalDTO;
import com.project.nyang.modules.animal.dto.AnimalListDTO;
import com.project.nyang.modules.animal.entity.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * AnimalRepository입니다
 *
 *
 * @author : 엄아영
 * @fileName : AnimalRepository
 * @since : 2025-07-09
 */


public interface AnimalRepository extends JpaRepository<Animal, String> {

    //페이징 전체 목록
    @Query("""
        SELECT new com.project.nyang.modules.animal.dto.AnimalListDTO(
            a.desertionNo,
            a.processState,
            a.sexCd,
            a.kindFullNm,
            a.noticeNo,
            a.happenDt,
            a.happenPlace,
            a.popfile1
        )
        FROM Animal a
        ORDER BY a.happenDt DESC
    """)
    Page<AnimalListDTO> findAllAnimals(Pageable pageable);
    //페이징 처리 결과를 담는 페이징 객체입니다.
}
