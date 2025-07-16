package com.project.nyang.modules.shelter.service;

import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.modules.shelter.dto.ShelterDetailDTO;
import com.project.nyang.modules.shelter.dto.ShelterListDTO;
import com.project.nyang.modules.shelter.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 보호소 관련 비즈니스 로직을 담당하는 Service 클래스입니다.
 * Repository에서 조회한 보호소 정보를 그대로 반환합니다.
 * @author : 오승훈
 * @fileName : ShelterService
 * @since : 2025-07-10
 */
@Service
@RequiredArgsConstructor
public class ShelterService {
    private final ShelterRepository shelterRepository;


    //보호소 전체 목록을 페이징하여 조회하는 메서드
    public Page<ShelterListDTO> getAllShelters(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return shelterRepository.findAllShelters(pageable); // 이미 DTO로 매핑되어 반환됨
    }

    //필터 조건(regionName, subRegionName)에 따라 보호소를 조회하는 메서드
    public Page<ShelterListDTO> filterShelters(
            int page, int size, String regionName, String subRegionName,String careName) {

        Pageable pageable = PageRequest.of(page, size);

        //지역 필터와 연동하여 보호소 이름으로 검색 기능 추가하기 위해 모든 지역 필터 기능에 조건문 추가
        // 전체 지역인 경우 → 전체 보호소
        if (regionName == null || regionName.equals("전체 지역")) {
            if (careName != null && !careName.isBlank()) {
                return shelterRepository.findByCareNameContaining(careName, pageable);
            }
            return shelterRepository.findAllShelters(pageable);
        }

        // 시/도는 선택됐고, 시/군/구는 전체 → 시/도 필터만 적용
        if (subRegionName == null || subRegionName.equals("전체")) {
            if (careName != null && !careName.isBlank()) {
                return shelterRepository.findByRegionAndCareName(regionName, careName, pageable);
            }
            return shelterRepository.findByRegion(regionName, pageable);
        }

        // 시/도 + 시/군/구 모두 선택 → 둘 다 필터
        if (careName != null && !careName.isBlank()) {
            return shelterRepository.findByRegionAndSubRegionAndCareName(regionName, subRegionName, careName, pageable);
        }
        return shelterRepository.findByRegionAndSubRegion(regionName, subRegionName, pageable);
    }

    // 보호소 상세 정보 조회 로직
    public ShelterDetailDTO getShelterDetail(String careRegNumber) {
        // repository 호출 후 결과 없으면 예외 발생
        return shelterRepository.findDetailByCareRegNumber(careRegNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.SHELTER_NOT_FOUND));
    }
}