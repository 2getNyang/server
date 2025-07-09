package com.project.nyang.modules.animal.service;

import com.project.nyang.modules.animal.dto.AnimalDTO;
import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.animal.repository.AnimalDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : 이지은
 * @fileName : AnimalDetailService
 * @since : 25. 7. 9.
 */
@Service
@RequiredArgsConstructor
public class AnimalDetailService {
    private final AnimalDetailRepository animalDetailRepository;

    @Transactional
    public AnimalDTO getAnimalDetail(String desertionNo) {
        Animal animal = animalDetailRepository.findByDesertionNo(desertionNo)
                .orElseThrow(() -> new RuntimeException("해당 동물을 찾을 수 없습니다."));
        return toDTO(animal);
    }

    // Entity → DTO 변환
    private AnimalDTO toDTO(Animal animal) {
        AnimalDTO dto = new AnimalDTO();
        dto.setDesertionNo(animal.getDesertionNo());
        dto.setHappenDt(animal.getHappenDt());
        dto.setHappenPlace(animal.getHappenPlace());
        dto.setKindFullNm(animal.getKindFullNm());
        dto.setColorCd(animal.getColorCd());
        dto.setAge(animal.getAge());
        dto.setWeight(animal.getWeight());
        dto.setNoticeNo(animal.getNoticeNo());
        dto.setNoticeSdt(animal.getNoticeSdt());
        dto.setNoticeEdt(animal.getNoticeEdt());
        dto.setPopfile1(animal.getPopfile1());
        dto.setPopfile2(animal.getPopfile2());
        dto.setPopfile3(animal.getPopfile3());
        dto.setProcessState(animal.getProcessState());
        dto.setSexCd(animal.getSexCd());
        dto.setNeuterYn(animal.getNeuterYn());
        dto.setSpecialMark(animal.getSpecialMark());
        return dto;
    }
}