package com.project.nyang.modules.shelter.controller;

import com.project.nyang.modules.shelter.dto.ShelterListDTO;
import com.project.nyang.modules.shelter.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ShelterController에 대한 클래스입니다.
 *
 * @author : 오승훈
 * @fileName : ShelterController
 * @since : 2025-07-10
 */
@Tag(name = "보호소 API", description = "보호소 정보 조회 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/shelters")
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterService;

    @Operation(
            summary = "보호소 전체 목록 조회 (페이징)",
            description = """
            보호소 전체 목록을 페이징하여 조회합니다.  
            기본적으로 한 페이지에 12개의 보호소를 반환하며,  
            page는 0부터 시작합니다.
            """
    )
    //보호소 전체 목록을 페이징해서 조회하는 API
    @GetMapping
    public Page<ShelterListDTO> getShelters(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지당 항목 수", example = "12")
            @RequestParam(defaultValue = "12") int size
    ) {
        return shelterService.getAllShelters(page, size);
    }
}