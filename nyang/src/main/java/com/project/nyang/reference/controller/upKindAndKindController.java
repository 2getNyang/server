package com.project.nyang.reference.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.reference.dto.KindDTO;
import com.project.nyang.reference.dto.SubRegionDTO;
import com.project.nyang.reference.dto.UpKindDTO;
import com.project.nyang.reference.repository.KindRepository;
import com.project.nyang.reference.service.UpkindAndKindService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 축종, 품종 검색 필터링 컨트롤러입니다.
 *
 * @author : 선순주
 * @fileName : upKindAndKindController
 * @since : 2025-07-22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/upKinds")
public class upKindAndKindController  {
    private final UpkindAndKindService upkindAndKindService;

    /**
     * 전체 축종 목록 조회
     */
    @Operation(summary = "모든 축종 목록 조회", description = "축종 코드와 이름을 리스트로 반환합니다.")
    @GetMapping
    public ResponseEntity<List<UpKindDTO>> getAllUpKinds() {
        List<UpKindDTO> upKinds = upkindAndKindService.getAllUpKinds();
        return ResponseEntity.ok(upKinds);
    }

    /**
     * 선택된 축종에 해당하는 품종 목록 조회
     */
    @Operation(
            summary = "선택된 축종에 해당하는 품종 목록 조회",
            description = "선택된 축종에 해당하는 품종 목록을 조회합니다."
    )
    @GetMapping("/{upKindName}")
    public ResponseEntity<List<KindDTO>> getKindsByUpKind(
            @Parameter(description = "축종 이름", example = "고양이")
            @PathVariable String upKindName
    ) {
        List<KindDTO> kinds = upkindAndKindService.getKindsByUpKind(upKindName);
        return ResponseEntity.ok(kinds);
    }


}