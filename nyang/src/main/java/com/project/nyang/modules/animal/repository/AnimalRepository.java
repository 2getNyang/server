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
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * AnimalRepository입니다
 *
 *
 * @author : 엄아영, 이지은
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
            AND (:upKindCd IS NULL OR a.upKind.upKindCd = :upKindCd)
            AND (:kindCd IS NULL OR a.kind.kindCd = :kindCd)
            AND (:regionCode IS NULL OR a.shelter.region.regionCode = :regionCode)
            AND (:subRegionCode IS NULL OR a.shelter.subRegion.subRegionCode = :subRegionCode)
    """)
    Page<AnimalListDTO> getFilterAnimals(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate,
                                         @Param("upKindCd") String upKindCd,
                                         @Param("kindCd") String kindCd,
                                         @Param("regionCode") String regionCode,
                                         @Param("subRegionCode") String subRegionCode,
                                         PageRequest pageable);

    Optional<Animal> findByDesertionNo(String desertionNo);

    //이달의 추천 동물
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
    LEFT JOIN LikeIt l ON l.animal = a
    WHERE a.noticeEdt >= CURRENT_DATE
    GROUP BY a.desertionNo, a.noticeEdt
    ORDER BY 
        DATEDIFF(a.noticeEdt, CURRENT_DATE) ASC, 
        COUNT(l.likeId) ASC,
        FUNCTION('RAND')
    """)
    List<AnimalListDTO> findRecommendAnimals(Pageable pageable);

    //DB에 있는 모든 유기동물 찾기
    @Query("""
        SELECT a.desertionNo FROM Animal a
    """)
    List<String> findAllDesertionNos();

    //가장 오래된 발견 일자 찾기
    @Query("""
        SELECT MIN(a.happenDt) FROM Animal a
    """)
    LocalDate findOldestHappenDt();

    // 가장 오래된 발견일자의 모든 동물 정보 불러오기
    List<Animal> findAllByHappenDt(LocalDate oldestDate);


    List<Animal> findByDesertionNoIn(Set<String> desertionNos);

    @Query("""
        SELECT a 
            FROM Animal a
            LEFT JOIN FETCH a.shelter s
            LEFT JOIN FETCH s.subRegion sr
            LEFT JOIN FETCH sr.region r
            LEFT JOIN FETCH a.upKind
            LEFT JOIN FETCH a.kind
        WHERE a.desertionNo IN :desertionNos
    """)
    List<Animal> findByDesertionNoInWithRegionAndSubRegion(@Param("desertionNos") Set<String> desertionNos);
}
