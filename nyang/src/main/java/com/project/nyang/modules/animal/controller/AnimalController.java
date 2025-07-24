package com.project.nyang.modules.animal.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.animal.dto.AnimalDTO;
import com.project.nyang.modules.animal.dto.AnimalDashboardDTO;
import com.project.nyang.modules.animal.dto.AnimalListDTO;
import com.project.nyang.modules.animal.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * AnimalController입니다
 *
 * @author : 엄아영
 * @fileName : AnimalController
 * @since : 2025-07-09
 */

@Tag(name = "Animal API", description = "동물 정보 관련 기능 (동물 전체 조회, 검색, 상세보기)")
@RestController
@RequestMapping("/api/v1/animals")
@RequiredArgsConstructor
@Slf4j
public class AnimalController {

    private final AnimalService animalService;

    //전체 유기동물 조회 (페이징)
    //가져와야하는 정보: 유기 동물 번호, 공고 상태, 성별, 동물 종류 전체 이름, 공고번호, 발견일, 발견장소, 이미지1
    @Operation(summary = "전체 유기동물 조회", description = "공공API에서 정보를 추려 가져와서 전체 동물을 조회")
    @GetMapping()
    public ResponseEntity<ApiResponse<Page<AnimalListDTO>>> getAnimals(
            @Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 당 데이터 개수", example = "12") @RequestParam(defaultValue = "12") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<AnimalListDTO> animal = animalService.getAnimals(pageable);
        return ResponseEntity.ok(ApiSuccessResponse.success(animal, "전체 동물 정보 조회에 성공하였습니다"));
    }

    //전체 유기동물 필터 검색 (페이징)
    @Operation(summary = "유기동물 필터 검색", description = "전체 동물 중 공고시작일, 축종 및 품종, 동물 보호 지역에 따라 필터 검색")
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<AnimalListDTO>>> getFilterAnimals(
            @Parameter(description = "시작일", example = "2025-07-11") @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "종료일", example = "2025-07-20") @RequestParam(required = false) LocalDate endDate,
            @Parameter(description = "축종 코드") @RequestParam(required = false) String upKindCd,
            @Parameter(description = "품종 코드") @RequestParam(required = false) String kindCd,
            @Parameter(description = "시도 코드", example = "6260000") @RequestParam(required = false) String regionCode,
            @Parameter(description = "시군구 코드", example = "3340000") @RequestParam(required = false) String subRegionCode,
            @Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 당 데이터 개수", example = "12") @RequestParam(defaultValue = "12") int size) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<AnimalListDTO> animal = animalService.getFilterAnimals(startDate, endDate, upKindCd, kindCd, regionCode, subRegionCode, pageable);

        return ResponseEntity.ok(ApiSuccessResponse.success(animal, "동물 필터 검색에 성공하였습니다"));
    }

    //상세 유기동물 조회
    @Operation(summary = "상세 유기동물 조회", description = "유기동물 고유 번호를 바탕으로 해당 동물의 상세 정보를 조회합니다.")
    @GetMapping("/{desertionNo}")
    public ResponseEntity<ApiResponse<AnimalDTO>> getAnimalDetail(@PathVariable String desertionNo,@AuthenticationPrincipal CustomUserDetails userDetails){
        Long userId = null;

        if (userDetails != null) {
            userId = userDetails.getId();
        }

        AnimalDTO animal = animalService.getAnimalDetail(desertionNo, userId);
        System.out.println("북마크체크: "+animal.isBookmarked());

        return ResponseEntity.ok(ApiSuccessResponse.success(animal,"동물공고 상세 조회에 성공하였습니다"));
    }

    //메인페이지용 카운트갯수
    @GetMapping("/counts")
    public ResponseEntity<ApiResponse<AnimalDashboardDTO>> getCounts() {
        AnimalDashboardDTO result = animalService.getDashboardCounts();
        return ResponseEntity.ok(ApiSuccessResponse.success(result));
    }


}