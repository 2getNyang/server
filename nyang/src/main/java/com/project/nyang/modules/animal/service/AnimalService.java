package com.project.nyang.modules.animal.service;

import com.project.nyang.modules.animal.dto.AnimalDTO;
import com.project.nyang.modules.animal.dto.AnimalListDTO;
import com.project.nyang.modules.animal.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * AnimalService입니다
 *
 * @author : 엄아영
 * @fileName : AnimalService
 * @since : 2025-07-09
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimalService {

    private final AnimalRepository animalRepository;

    //페이징 전체 목록
    public Page<AnimalListDTO> getAnimals(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return animalRepository.findAllAnimals(pageable); //페이저블에 페이징에대한 정보를 담아서 레포지토리에 전달하는 역할
    }
}