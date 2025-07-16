package com.project.nyang.modules.animal.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.modules.animal.dto.AnimalListDTO;
import com.project.nyang.modules.animal.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AnimalRecommendationController입니다.
 *
 * @author : 엄아영
 * @fileName : AnimalRecommendationController
 * @since : 2025-07-14
 */

@Tag(name = "AnimalRecommendation API", description = "동물 추천 기능")
@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class AnimalRecommendationController {

    private final AnimalService animalService;

    // 이달의 추천 동물
    @Operation(summary = "이달의 추천 동물", description = "오늘 날짜를 기준으로 잔여 공고일이 가장 적은 동물부터, 동률 시 찜(좋아요) 수가 적은 순서로 정렬한다. \n" +
            "이렇게 우선순위를 적용한 뒤 상위 limit 6마리만 잘라서 응답한다.")
    @GetMapping()
    public ResponseEntity<ApiResponse<List<AnimalListDTO>>> getRecommendAnimals(){
        List<AnimalListDTO> recommendations = animalService.getRecommendAnimals();
        return ResponseEntity.ok(ApiSuccessResponse.success(recommendations, "추천동물 조회에 성공하였습니다."));
    }
}