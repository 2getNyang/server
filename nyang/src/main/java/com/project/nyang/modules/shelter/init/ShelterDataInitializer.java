//package com.project.nyang.modules.shelter.init;
//
//import com.project.nyang.modules.shelter.service.ShelterApiService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
///**
// * 보호소 정보 신규 저장 우선순위 설정을 위한 ApplicationRunner 클래스입니다.
// *
// * @author : 오승훈
// * @fileName : ShelterDataInitializer
// * @since : 2025-07-17
// */
//@Slf4j
//@Component
//@Order(1) // 실행 순서 지정 (여러 Runner가 있을 경우 우선순위 조정 가능)
//@RequiredArgsConstructor
//public class ShelterDataInitializer implements ApplicationRunner {
//
//    private final ShelterApiService shelterApiService;
//
//    @Override
//    public void run(ApplicationArguments args) {
//        log.info("ApplicationRunner를 통해 보호소 초기 데이터 확인 및 수집 시작");
//        shelterApiService.initializeIfEmpty();
//    }
//}