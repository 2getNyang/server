package com.project.nyang.modules.animal.scheduler;

import com.project.nyang.modules.animal.controller.AnimalApiController;
import com.project.nyang.modules.animal.service.AnimalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * AnimalDataScheduler입니다
 *
 * @author : 엄아영
 * @fileName : AnimalDataScheduler
 * @since : 2025-07-15
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class AnimalDataScheduler {

    private final AnimalApiService animalApiService;
    private final AnimalApiController animalApiController;

    @Scheduled(cron = "0 0 1 * * *")
    public void refreshDailyData() {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("01시 스케줄링 - 오늘 동물 데이터 수집 : {}", todayStr);

        animalApiService.fetchAndSaveAnimals(todayStr, todayStr);
        animalApiService.updateAnimals(todayStr, todayStr);

        log.info("01시 스케줄링 - 오늘 동물 데이터 수집 완료");
    }

    @Scheduled(cron = "0 0 13 * * *")
    public void refreshDailyDataAfternoon() {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("13시 스케줄링 - 오늘 동물 데이터 수집 : {}", todayStr);

        animalApiService.fetchAndSaveAnimals(todayStr, todayStr);
        animalApiService.updateAnimals(todayStr, todayStr);

        log.info("13시 스케줄링 - 오늘 동물 데이터 수집 완료");
    }

    //스케줄링 테스트
//    @Scheduled(cron = "*/30 * * * * *")  // 매 30초마다
//    public void refreshSecondData() {
//        LocalDate today = LocalDate.now();
//        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        log.info("30초씩 스케줄링 - 오늘 데이터 수집 : {}", todayStr);
//
//        animalApiService.fetchAndSaveAnimals(todayStr, todayStr);
//        animalApiService.updateAnimals(todayStr, todayStr);
//    }
}