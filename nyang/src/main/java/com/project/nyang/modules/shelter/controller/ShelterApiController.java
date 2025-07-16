package com.project.nyang.modules.shelter.controller;
import com.project.nyang.modules.shelter.service.ShelterApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * scheduler 테스트용 컨트롤러에 대한 클래스입니다.
 *
 * @author : 오승훈
 * @fileName : ShelterApiController
 * @since : 2025-07-16
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/public/shelter")
@RequiredArgsConstructor
public class ShelterApiController {
    private final ShelterApiService shelterApiService;

    /**
     * 보호소 정보 갱신 트리거 (테스트용)
     */
    @GetMapping("/update")
    public ResponseEntity<String> updateShelterManually() {
        log.info("📥 보호소 API 수동 호출 시작");
        shelterApiService.fetchAndUpdateShelters();
        return ResponseEntity.ok("보호소 공공데이터 수집 및 갱신이 완료되었습니다.");
    }
}