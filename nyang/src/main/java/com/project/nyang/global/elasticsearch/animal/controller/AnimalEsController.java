package com.project.nyang.global.elasticsearch.animal.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.elasticsearch.animal.dto.AnimalEsListDTO;
import com.project.nyang.global.elasticsearch.animal.service.AnimalEsService;
import com.project.nyang.modules.animal.dto.AnimalListDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * AnimalEsController입니다.
 *
 * @author : 엄아영
 * @fileName : AnimalEsController
 * @since : 2025-07-17
 */
@Tag(name = "Animal elastic search API", description = "동물 정보 elastic search 관련 기능")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/animals")
public class AnimalEsController {

    private final AnimalEsService  animalEsService;

    @Operation(summary = "유기 동물 정보 키워드 검색")
    @GetMapping("/elasticsearch")
    public ResponseEntity<ApiResponse<Page<? extends AnimalEsListDTO>>> searchEsAnimals(
            @Parameter(description = "검색어", example = "전남-함평-2025-00278") @RequestParam String keyword,
            @Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "한 페이지에 보여줄 게시물 개수", example = "12") @RequestParam(defaultValue = "12") int size) {

        return ResponseEntity.ok(ApiSuccessResponse.success(animalEsService.searchEsAnimals(keyword, page, size), "키워드 기반 동물 검색 성공"));
    }

    @Operation(summary = "유기 동물 정보 통합검색 (키워드+필터)")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AnimalEsListDTO>>> searchWithKeywordAndFilter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String upKindNm,
            @RequestParam(required = false) String kindNm,
            @RequestParam(required = false) String regionName,
            @RequestParam(required = false) String subRegionName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<AnimalEsListDTO> results = animalEsService.searchWithKeywordAndFilter(keyword, startDate, endDate, upKindNm, kindNm, regionName, subRegionName, pageable);
        return ResponseEntity.ok(ApiSuccessResponse.success(results, "통합 검색 성공"));
    }
}