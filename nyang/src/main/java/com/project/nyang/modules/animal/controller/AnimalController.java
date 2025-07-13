package com.project.nyang.modules.animal.controller;

import com.project.nyang.modules.animal.dto.AnimalDTO;
import com.project.nyang.modules.animal.dto.AnimalListDTO;
import com.project.nyang.modules.animal.service.AnimalService;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * AnimalController입니다
 *
 * @author : 엄아영
 * @fileName : AnimalController
 * @since : 2025-07-09
 */

@RestController
@RequestMapping("/api/v1/animals")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    //전체 유기동물 조회
    //가져와야하는 정보: 유기 동물 번호, 공고 상태, 성별, 동물 종류 전체 이름, 공고번호, 발견일, 발견장소, 이미지1
    @GetMapping()
    public Page<AnimalListDTO> getAnimals(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "12") int size)  {
        return animalService.getAnimals(page, size);
    }

}