package com.project.nyang.modules.animal.controller;

import com.project.nyang.modules.animal.dto.AnimalDTO;
import com.project.nyang.modules.animal.service.AnimalDetailService;
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

    @GetMapping("/{desertionNo}")
    public ResponseEntity<AnimalDTO> getAnimalDetail(@PathVariable String desertionNo){
    return ResponseEntity.ok(animalDetailService.getAnimalDetail(desertionNo));
    }
}