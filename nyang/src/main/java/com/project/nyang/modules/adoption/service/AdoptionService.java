package com.project.nyang.modules.adoption.service;

import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.modules.adoption.dto.AdoptionDTO;
import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import com.project.nyang.modules.adoption.repository.AdoptionRepository;
import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.animal.repository.AnimalRepository;
import com.project.nyang.modules.shelter.entity.Shelter;
import com.project.nyang.modules.shelter.repository.ShelterRepository;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 입양신청서 데이터를 처리하는 서비스입니다
 *
 * @author : 이지은
 * @fileName : AdoptionService
 * @since : 25. 7. 14.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdoptionService {

    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final ShelterRepository shelterRepository;
    private final AdoptionRepository adoptionRepository;

    public void processAdoptionApplication(AdoptionDTO dto) {
        // 1. DB 저장
       AdoptionDTO application = saveApplication(dto);
       System.out.println("입양신청서의 DB 저장이 완료되었습니다.");

        // 2. PDF 생성
        //File pdf = generatePdf(application);

        // 3. 보호소 이메일 조회
        //String shelterEmail = findShelterEmail(application.getAnimalId());

        // 4. 이메일 전송
        //sendEmailWithAttachment(shelterEmail, pdf);
    }

    private AdoptionDTO saveApplication(AdoptionDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));

        Shelter shelter = shelterRepository.findByCareRegNumber(dto.getCareRegNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_SHELTER));

        Animal animal = animalRepository.findByDesertionNo(dto.getDesertionNo())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_ANIMAL));

        PetApplicationForm form = PetApplicationForm.builder()
                .userName(dto.getUserName())
                .userBirth(dto.getUserBirth())
                .userGender(dto.getUserGender())
                .userPhone(dto.getUserPhone())
                .familyPhone(dto.getFamilyPhone())
                .family(dto.getFamily())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .job(dto.getJob())
                .experience(dto.getExperience())
                .applicationReason(dto.getApplicationReason())
                .user(user)
                .shelter(shelter)
                .animal(animal)
                .build();

        PetApplicationForm saved = adoptionRepository.save(form);

        return toDto(saved);
    }

    public AdoptionDTO toDto(PetApplicationForm form) {
        return AdoptionDTO.builder()
                .id(form.getId())
                .userName(form.getUserName())
                .userBirth(form.getUserBirth())
                .userGender(form.getUserGender())
                .userPhone(form.getUserPhone())
                .familyPhone(form.getFamilyPhone())
                .family(form.getFamily())
                .address(form.getAddress())
                .detailAddress(form.getDetailAddress())
                .job(form.getJob())
                .experience(form.getExperience())
                .formCreatedAt(form.getFormCreatedAt())
                .applicationReason(form.getApplicationReason())
                .desertionNo(form.getAnimal().getDesertionNo())
                .careRegNumber(form.getShelter().getCareRegNumber())
                .userId(form.getUser().getId())
                .build();
    }


}