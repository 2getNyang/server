package com.project.nyang.modules.shelter.service;

import com.project.nyang.modules.shelter.dto.ShelterListDTO;
import com.project.nyang.modules.shelter.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
}