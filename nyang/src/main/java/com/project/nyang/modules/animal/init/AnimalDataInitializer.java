//package com.project.nyang.modules.animal.init;
//
//import com.project.nyang.modules.animal.service.AnimalApiService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
///**
// * 동물 정보 신규 저장 우선순위 설정을 위한 ApplicationRunner 클래스입니다.
// *
// * @author : 오승훈
// * @fileName : AnimalDataInitializer
// * @since : 2025-07-17
// */
//@Slf4j
//@Component
//@Order(2)  // 뒤에 실행됨
//@RequiredArgsConstructor
//public class AnimalDataInitializer implements ApplicationRunner {
//    private final AnimalApiService animalApiService;
//
//    @Override
//    public void run(ApplicationArguments args) {
//        log.info("ApplicationRunner를 통해 동물 초기 데이터 확인 및 수집 시작");
//        animalApiService.initialize();
//    }
////