package com.project.nyang.modules.adoption.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.adoption.dto.AdoptionDTO;
import com.project.nyang.modules.adoption.service.AdoptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/{desertionNo}")
    ResponseEntity<ApiResponse<String>> applyForAdoption(@PathVariable String desertionNo, @RequestBody AdoptionDTO request) {

        // 사용자 정보 포함한 desertionNo로 중복 신청 여부 확인
        boolean alreadyApplied = adoptionService.hasAlreadyApplied(request.getUserId(), desertionNo);
        if (alreadyApplied) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiSuccessResponse.error(40901, "이미 해당 공고에 입양 신청하셨습니다."));
        }

        //신청 진행
        //desertionNo를 DTO에 세팅
        AdoptionDTO updatedRequest = request.toBuilder()
                .desertionNo(desertionNo)
                .build();
        adoptionService.processAdoptionApplication(updatedRequest);
        return ResponseEntity.ok(ApiSuccessResponse.success(null, "입양 신청이 접수되었습니다."));
    }

    @Operation(summary = "입양 신청 재요청", description = "입양 신청 재요청을 처리합니다.")
    @PostMapping("/{formId}/resend")
    ResponseEntity<ApiResponse<String>> resendAdoption(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long formId) {
        Long userId = userDetails.getId();
        adoptionService.reprocessAdoptionApplication(formId, userId);
        return ResponseEntity.ok(ApiSuccessResponse.success(null, "입양 신청이 재전송되었습니다."));
    }

}