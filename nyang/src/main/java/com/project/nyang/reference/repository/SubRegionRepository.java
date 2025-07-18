package com.project.nyang.reference.repository;

import com.project.nyang.reference.dto.SubRegionDTO;
import com.project.nyang.reference.entity.Region;
import com.project.nyang.reference.entity.SubRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 시군구 레포지토리입니다.
 *
 * @author : 선순주
 * @fileName : SubRegionRepository
 * @since : 2025-07-11
 */
public interface SubRegionRepository extends JpaRepository<SubRegion, String> {
    Optional<SubRegion> findBySubRegionName(String subRegionName);

    /**
     * 선택된 시도에 해당하는 시군구 목록 조회
     */
    @Query("SELECT new com.project.nyang.reference.dto.SubRegionDTO(sr.subRegionName) " +
            "FROM SubRegion sr " +
            "WHERE sr.region.regionName = :regionName")
    List<SubRegionDTO> findSubRegionsByRegion(String regionName);

    Optional<SubRegion> findByRegionAndSubRegionName(Region region, String subRegionName);

    Optional<SubRegion> findBySubRegionCode(String subRegionCode);
    List<SubRegion> findByRegion_RegionCode(String regionCode);

    // 수정: 다건 조회
    List<SubRegion> findAllBySubRegionName(String subRegionName);
}
