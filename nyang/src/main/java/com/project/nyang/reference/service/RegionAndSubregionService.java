package com.project.nyang.reference.service;

import com.project.nyang.reference.dto.RegionDTO;
import com.project.nyang.reference.dto.SubRegionDTO;
import com.project.nyang.reference.repository.RegionRepository;
import com.project.nyang.reference.repository.SubRegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 시/도, 시/군/구 필터 해주기 위해 필요한 서비스에 대한 클래스입니다.
 *
 * @author : 오승훈
 * @fileName : RegionAndSubregionService
 * @since : 2025-07-15
 */
@Service
@RequiredArgsConstructor
public class RegionAndSubregionService {

    private final RegionRepository regionRepository;

    private final SubRegionRepository subRegionRepository;
    /**
     * 전체 시도 목록을 조회합니다.
     */
    public List<RegionDTO> getAllRegions() {
        return regionRepository.findAllRegions().stream()
                .map(regionName -> new RegionDTO(regionName))
                .toList();  // 리포지토리에서 바로 지역 목록을 가져오므로 서비스에서 별도로 DTO 변환이 필요 없음
    }


    /**
     * 선택된 시도에 해당하는 시군구 목록을 조회합니다.
     * @param regionName 시도 이름
     */
    public List<SubRegionDTO> getSubRegionsByRegion(String regionName) {
        return subRegionRepository.findSubRegionsByRegion(regionName); // 이미 DTO로 반환되므로 map 없이 반환
    }
}