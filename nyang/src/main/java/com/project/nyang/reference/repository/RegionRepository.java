package com.project.nyang.reference.repository;

import com.project.nyang.reference.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 시/도 레포지토리입니다.
 *
 * @author : 선순주
 * @fileName : RegionRepository
 * @since : 2025-07-11
 */
public interface RegionRepository extends JpaRepository<Region, String> {
    Optional<Region> findByRegionName(String regionName);
    /**
     * 모든 시도 이름을 조회하는 쿼리
     */
    @Query("SELECT r.regionName FROM Region r")
    List<String> findAllRegions();
}
