package com.project.nyang.modules.adoption.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.modules.adoption.dto.AdoptionDTO;
import com.project.nyang.modules.adoption.service.AdoptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 입양 신청 관련 기능이 포함되어 있는 컨트롤러 입니다
 *
 * @author : 이지은
 * @fileName : AdoptionController
 * @since : 25. 7. 14.
 */
@Tag(name = "Adoption API",description = "동물 입양 신청 관련 기능")
@RestController
@RequestMapping("/api/v1/adoptions")
@RequiredArgsConstructor
public class AdoptionController {

    private final AdoptionService adoptionService;

    @Operation(summary = "Form에서 받아온 정보 처리", description = "사용자가 작성한 입양신청를 처리합니다.")
    @PostMapping
    ResponseEntity<ApiResponse<String>> applyForAdoption(@RequestBody AdoptionDTO request) {
        adoptionService.processAdoptionApplication(request);
        return ResponseEntity.ok(ApiSuccessResponse.success(null, "입양 신청이 접수되었습니다."));
    }
}