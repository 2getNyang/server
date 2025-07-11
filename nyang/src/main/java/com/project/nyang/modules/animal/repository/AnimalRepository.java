package com.project.nyang.modules.animal.repository;

import com.project.nyang.modules.animal.dto.AnimalDTO;
import com.project.nyang.modules.animal.dto.AnimalListDTO;
import com.project.nyang.modules.animal.entity.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

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
            a.popfile1,
            up.upKindCd,
            up.upKindNm,
            k.kindCd,
            k.kindNm,
            r.regionCode,
            r.regionName,
            sr.subRegionCode,
            sr.subRegionName
        )
        FROM Animal a
        JOIN a.upKind up
        JOIN a.kind k
        JOIN a.shelter s
        JOIN s.subRegion sr
        JOIN sr.region r
        ORDER BY a.happenDt DESC
    """)
    //페이징 처리 결과를 담는 페이징 객체입니다.
    Page<AnimalListDTO> findAllAnimals(Pageable pageable);

    //페이징 동물 필터 검색
    @Query("""
        SELECT new com.project.nyang.modules.animal.dto.AnimalListDTO(
            a.desertionNo,
            a.processState,
            a.sexCd,
            a.kindFullNm,
            a.noticeNo,
            a.happenDt,
            a.happenPlace,
            a.popfile1,
            a.upKind.upKindCd,
            a.upKind.upKindNm,
            a.kind.kindCd,
            a.kind.kindNm,
            a.shelter.region.regionCode,
            a.shelter.region.regionName,
            a.shelter.subRegion.subRegionCode,
            a.shelter.subRegion.subRegionName
        )
        FROM Animal a
        WHERE a.noticeSdt <= :endDate
            AND a.noticeEdt >= :startDate
            AND (:upKindCd IS NULL OR a.upKind.upKindCd <=:upKindCd)
            AND (:kindCd IS NULL OR a.kind.kindCd <=:kindCd)
            AND (:regionCode IS NULL OR a.shelter.region.regionCode <=:regionCode)
            AND (:subRegionCode IS NULL OR a.shelter.subRegion.subRegionCode <=:subRegionCode)
    """)
    Page<AnimalListDTO> getFilterAnimals(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate,
                                         @Param("upKindCd") String upKindCd,
                                         @Param("kindCd") String kindCd,
                                         @Param("regionCode") String regionCode,
                                         @Param("subRegionCode") String subRegionCode,
                                         PageRequest pageable);
}
