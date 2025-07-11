package com.project.nyang.reference.repository;

import com.project.nyang.reference.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
