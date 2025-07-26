package com.project.nyang.modules.animal.controller;

import com.project.nyang.global.common.publicapi.PublicAnimalApiClient;
import com.project.nyang.modules.animal.dto.AnimalApiResponse;
import com.project.nyang.modules.animal.service.AnimalApiService;
import com.project.nyang.modules.animal.service.AnimalDataSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * AnimalApiController입니다
 *
 * @author : 엄아영
 * @fileName : AnimalApiController
 * @since : 2025-07-14
 */

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class AnimalApiController {

    private final AnimalApiService animalApiService;
    private final PublicAnimalApiClient publicAnimalApiClient;
    private final AnimalDataSyncService animalDataSyncService;

    //테스트
    @GetMapping("/test")
    public void testFetchAnimals(@RequestParam String startDate, @RequestParam String endDate) {
        animalApiService.testFetchAnimals(startDate, endDate);
    }

    //api 불러와서 db에 저장
    @GetMapping()
    public ResponseEntity<String> getPublicAnimals(@RequestParam String startDate, @RequestParam String endDate) {
        animalApiService.fetchAndSaveAnimals(startDate, endDate);
        return ResponseEntity.ok("공공데이터 API의 동물 정보를 성공적으로 조회하고 저장하였습니다.");
    }

    //업데이트 된 api 불러와서 db에 저장
    @GetMapping("/update")
    public ResponseEntity<String> getUpdateAnimals(@RequestParam String startDate, @RequestParam String endDate) {
        animalApiService.updateAnimals(startDate, endDate);
        return ResponseEntity.ok("공공데이터 API의 동물 정보를 성공적으로 업데이트하였습니다.");
    }

    //db와 es 인덱스 비교
    @GetMapping("/compare")
    public ResponseEntity<?> compareDbAndEs() {
        Map<String, Object> result = animalDataSyncService.compareDbAndEsIndex();
        return ResponseEntity.ok(result);
    }
}
