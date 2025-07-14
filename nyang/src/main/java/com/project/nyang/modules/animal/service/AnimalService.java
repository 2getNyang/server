package com.project.nyang.modules.animal.service;

import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.modules.animal.dto.AnimalDTO;
import com.project.nyang.modules.animal.dto.AnimalListDTO;
import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.animal.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * AnimalService입니다
 *
 * @author : 엄아영, 이지은
 * @fileName : AnimalService
 * @since : 2025-07-09
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimalService {

    private final AnimalRepository animalRepository;

    //페이징 전체 목록
    @Transactional
    public Page<AnimalListDTO> getAnimals(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return animalRepository.findAllAnimals(pageable); //페이저블에 페이징에대한 정보를 담아서 레포지토리에 전달하는 역할
    }

    //페이징 동물 필터 검색
    @Transactional
    public Page<AnimalListDTO> getFilterAnimals(LocalDate startDate, LocalDate endDate,
                                                String upKindCd, String kindCd,
                                                String regionCode, String subRegionCode,
                                                PageRequest pageable) {

        //시작일과 종료일이 선택되지 않으면 예외처리
        //프론트에서도 막아둬야함
        if(startDate == null || endDate == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        //사용자가 선택한 시작 날짜가 종료일보다 이전이어야함
        if (startDate.isAfter(endDate)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        // 축종이 null인데 품종 값이 들어왔을 경우 예외 처리
        if(upKindCd == null && kindCd != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        // 시도가 null인데 시군구 값이 들어왔을 경우 예외 처리
        if(regionCode == null && subRegionCode != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        return animalRepository.getFilterAnimals(startDate, endDate, upKindCd, kindCd, regionCode, subRegionCode, pageable);
    }

    @Transactional
    public AnimalDTO getAnimalDetail(String desertionNo) {
        Animal animal = animalRepository.findByDesertionNo(desertionNo)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_ANIMAL));
        return toDTO(animal);
    }

    // Entity → DTO 변환
    private AnimalDTO toDTO(Animal animal) {
        return AnimalDTO.builder()
                .desertionNo(animal.getDesertionNo())
                .happenDt(animal.getHappenDt())
                .happenPlace(animal.getHappenPlace())
                .kindFullNm(animal.getKindFullNm())
                .colorCd(animal.getColorCd())
                .age(animal.getAge())
                .weight(animal.getWeight())
                .noticeNo(animal.getNoticeNo())
                .noticeSdt(animal.getNoticeSdt())
                .noticeEdt(animal.getNoticeEdt())
                .popfile1(animal.getPopfile1())
                .popfile2(animal.getPopfile2())
                .popfile3(animal.getPopfile3())
                .processState(animal.getProcessState())
                .sexCd(animal.getSexCd())
                .neuterYn(animal.getNeuterYn())
                .specialMark(animal.getSpecialMark())
                .build();
    }

    @Transactional
    public List<AnimalListDTO> getRecommendAnimals() {
        return animalRepository.findRecommendAnimals(PageRequest.of(0, 12));
    }
}