package com.project.nyang.modules.adoption.service;

import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.modules.adoption.dto.AdoptionDTO;
import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import com.project.nyang.modules.adoption.mail.service.MailService;
import com.project.nyang.modules.adoption.pdf.PdfGenerator;
import com.project.nyang.modules.adoption.repository.AdoptionRepository;
import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.animal.repository.AnimalRepository;
import com.project.nyang.modules.shelter.entity.Shelter;
import com.project.nyang.modules.shelter.repository.ShelterRepository;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

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
    private final PdfGenerator pdfGenerator;

    private final MailService mailService;

    @Operation(summary = "입양신청중복확인", description = "입양신청시 중복 신청인지 확인하는 메서드 입니다")
    public boolean hasAlreadyApplied(Long userId, String desertionNo) {
        return adoptionRepository.existsByUserIdAndAnimal_DesertionNo(userId, desertionNo);
    }

    @Operation(summary = "입양신청처리", description = "입양신청 데이터 처리 프로세스 메서드 입니다")
    public void processAdoptionApplication(AdoptionDTO dto) {

        //0.이미 신청했는지 확인하는 로직 추가, 신청하지 않았으면 아래 주석 번호 순대로 로직 실행

        // 1. DB 저장
       AdoptionDTO application = saveApplication(dto);
       System.out.println("입양신청서의 DB 저장이 완료되었습니다.");

        //2. 워드로 작성한 템플릿에 DTO 값 치환
        File pdfFile = pdfGenerator.htmlToPdf(application);

        //3. 보호소 이메일 조회
        String shelterEmail = findShelterEmail(application.getCareRegNumber());
        log.info("보호소 이메일 조회가 완료되었습니다: ",shelterEmail);

        //4. 이메일 전송
        mailService.sendEmailWithPdf(
                shelterEmail,
                "새 입양 신청서가 도착했습니다",
                "<p>새로운 입양 신청서가 접수되었습니다. 첨부된 PDF 파일을 확인하세요.</p>",
                pdfFile
        );

        //5. 저장된 PDF 삭제
        if (pdfFile.exists()) {
            boolean deleted = pdfFile.delete();
            if (deleted) {
                log.info("✅ 임시 PDF 파일 삭제 완료: {}", pdfFile.getAbsolutePath());
            } else {
                log.warn("⚠️ 임시 PDF 파일 삭제 실패: {}", pdfFile.getAbsolutePath());
            }
        }
    }

    @Operation(summary = "입양신청서 저장", description = "입양신청서의 작성 답변을 DB에 저장합니다.")
    private AdoptionDTO saveApplication(AdoptionDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));

        Shelter shelter = shelterRepository.findByCareRegNumber(dto.getCareRegNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.SHELTER_NOT_FOUND));

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
                .adultCount(dto.getAdultCount())
                .childrenCount(dto.getChildrenCount())
                .allConsent(dto.getAllConsent())
                .housingType(dto.getHousingType())
                .hasAllergy(dto.getHasAllergy())
                .hasOtherPets(dto.getHasOtherPets())
                .consentForCheck(dto.getConsentForCheck())
                .noticeNo(animal.getNoticeNo())
                .user(user)
                .shelter(shelter)
                .animal(animal)
                .build();

        PetApplicationForm saved = adoptionRepository.save(form);

        return toDto(saved);
    }
    @Operation(summary = "DTO변환", description = "PetApplicationForm Entity를 DTO로 변환하는 메서드입니다.")
    public AdoptionDTO toDto(PetApplicationForm form) {
        return AdoptionDTO.builder()
                .formId(form.getFormId())
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
                .adultCount(form.getAdultCount())
                .childrenCount(form.getChildrenCount())
                .allConsent(form.getAllConsent())
                .housingType(form.getHousingType())
                .hasAllergy(form.getHasAllergy())
                .hasOtherPets(form.getHasOtherPets())
                .consentForCheck(form.getConsentForCheck())
                .desertionNo(form.getAnimal().getDesertionNo())
                .careRegNumber(form.getShelter().getCareRegNumber())
                .userId(form.getUser().getId())
                .noticeNo(form.getNoticeNo())
                .build();
    }

    @Operation(summary = "보호소이메일조회", description = "이메일 전송에 필요한 보호소 이메일 정보를 조회하는 메서드 입니다.")
    private String findShelterEmail(String careRegNumber) {
        Shelter shelter = shelterRepository.findByCareRegNumber(careRegNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.SHELTER_EMAIL_NOT_FOUND));

        String email = shelter.getCareEmail(); // 실제 필드명에 맞게 변경
        if (email == null || email.isEmpty()) {
            throw new CustomException(ErrorCode.SHELTER_EMAIL_NOT_FOUND);
        }
        return email;
    }

}