package com.project.nyang.reference.repository;

import com.project.nyang.reference.entity.SubRegion;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
