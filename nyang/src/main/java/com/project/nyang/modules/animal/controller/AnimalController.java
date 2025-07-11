package com.project.nyang.modules.animal.controller;

import com.project.nyang.modules.animal.dto.AnimalDTO;
import com.project.nyang.modules.animal.dto.AnimalListDTO;
import com.project.nyang.modules.animal.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
public class AnimalController {

    private final AnimalService animalService;

    //가져와야하는 정보: 유기 동물 번호, 공고 상태, 성별, 동물 종류 전체 이름, 공고번호, 발견일, 발견장소, 이미지1
    @Operation(summary = "전체 유기동물 조회", description = "공공API에서 정보를 추려 가져와서 전체 동물을 조회")
    @GetMapping()
    public Page<AnimalListDTO> getAnimals(@Parameter(description = "페이지 번호", example = "0")
                                          @RequestParam(defaultValue = "0") int page,
                                          @Parameter(description = "페이지 당 데이터 개수", example = "12")
                                          @RequestParam(defaultValue = "12") int size)  {
        PageRequest pageable = PageRequest.of(page, size);
        return animalService.getAnimals(page, size);
    }
}