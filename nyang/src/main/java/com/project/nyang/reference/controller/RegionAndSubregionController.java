package com.project.nyang.reference.controller;

import com.project.nyang.reference.dto.RegionDTO;
import com.project.nyang.reference.dto.SubRegionDTO;
import com.project.nyang.reference.service.RegionAndSubregionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 시/도, 시/군/구 필터 해주기위해서 필요한 지역 이름 전체를 넘겨주는 컨트롤러입니다.
 *
 * @author : 오승훈
 * @fileName : RegionAndSubregionController
 * @since : 2025-07-15
 */
@Tag(name = "지역 API", description = "시/도 및 시/군/구 정보 조회 API입니다.")
@RestController
@RequestMapping("/api/v1/regions")
@RequiredArgsConstructor
public class RegionAndSubregionController {

    private final RegionAndSubregionService regionAndSubregionService;

    /**
     * 전체 시도 목록 조회
     */
    @Operation(
            summary = "전체 시도 목록 조회",
            description = "시도 이름 목록을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<RegionDTO>> getAllRegions() {
        List<RegionDTO> regions = regionAndSubregionService.getAllRegions();
        return ResponseEntity.ok(regions);
    }

    /**
     * 선택된 시도에 해당하는 시군구 목록 조회
     * @param regionName 시도 이름
     */
    @Operation(
            summary = "선택된 시도에 해당하는 시군구 목록 조회",
            description = "주어진 시도 이름에 맞는 시군구 목록을 조회합니다."
    )
    @GetMapping("/{regionName}")
    public ResponseEntity<List<SubRegionDTO>> getSubRegionsByRegion(
            @Parameter(description = "시도 이름 (예: 서울특별시)", example = "서울특별시")
            @PathVariable String regionName
    ) {
        List<SubRegionDTO> subRegions = regionAndSubregionService.getSubRegionsByRegion(regionName);
//        System.out.println(subRegions);
        return ResponseEntity.ok(subRegions);
    }
}