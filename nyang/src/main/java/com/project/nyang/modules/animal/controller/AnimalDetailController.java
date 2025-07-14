package com.project.nyang.modules.animal.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.modules.animal.dto.AnimalDTO;
import com.project.nyang.modules.animal.service.AnimalDetailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * AnimalDetailController 상세조회 페이지의 기능 컨트롤러입니다.
 *
 * @author : 이지은
 * @fileName : AnimalDetailController
 * @since : 25. 7. 9.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/animals")
public class AnimalDetailController {

    private final AnimalDetailService animalDetailService;

    @Operation(summary = "상세 유기동물 조회", description = "유기동물 고유 번호를 바탕으로 해당 동물의 상세 정보를 조회합니다.")
    @GetMapping("/{desertionNo}")
    public ResponseEntity<ApiResponse<AnimalDTO>> getAnimalDetail(@PathVariable String desertionNo){
        AnimalDTO animal = animalDetailService.getAnimalDetail(desertionNo);
        return ResponseEntity.ok(ApiSuccessResponse.success(animal,"동물정보 조회에 성공하였습니다"));
    }
}