package com.project.nyang.modules.shelter.scheduler;

import com.project.nyang.modules.shelter.service.ShelterApiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * shelter 데이터를 갱신해주는 Scheduler 에 대한 클래스입니다.
 *
 * @author : 오승훈
 * @fileName : ShelterDataScheduler
 * @since : 2025-07-16
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ShelterDataScheduler {

    private final ShelterApiService shelterApiService;


    /**
     * 매월 1일 새벽 2시에 실행
     */
    @Scheduled(cron = "0 0 2 1 * *")
    public void monthlyUpdateShelterData() {
        log.info("🗂️ [Shelter Scheduler] 매월 1일 보호소 데이터 갱신 시작");
        shelterApiService.fetchAndUpdateShelters();
        log.info("✅ [Shelter Scheduler] 보호소 데이터 갱신 완료");
    }

//    @Scheduled(cron = "*/30 * * * * *")  // 매 30초마다
//    public void refreshSecondData() {
//        log.info("🗂️ [Shelter Scheduler] 30초 갱신 시작");
//        shelterApiService.fetchAndUpdateShelters();
//        log.info("✅ [Shelter Scheduler] 보호소 데이터 갱신 완");
//    }
}