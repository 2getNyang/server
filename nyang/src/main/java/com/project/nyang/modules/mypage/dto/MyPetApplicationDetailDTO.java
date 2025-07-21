package com.project.nyang.modules.mypage.dto;

import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 입양신청 내역 상세 조회 DTO
 *
 * @author : 박세정
 * @fileName : MyPetApplicationDetailDTO
 * @since : 2025-07-18
 */
@Getter
public class MyPetApplicationDetailDTO {

    @Schema(description = "입양 신청 내역 ID")
    private Long formId;

    @Schema(description = "공고 번호")
    private String noticeNo;

    @Schema(description = "신청자 이름")
    private String userName;

    @Schema(description = "신청자 생년월일")
    private LocalDate userBirth;

    @Schema(description = "신청자 성별")
    private PetApplicationForm.Gender userGender;

    @Schema(description = "신청자 휴대폰번호")
    private String userPhone;

    @Schema(description = "신청자 외 보호자 번호")
    private String familyPhone;

    @Schema(description = "familyPhone에 작성한 보호자와의 관계")
    private String family;

    @Schema(description = "주소")
    private String address;

    @Schema(description = "상세주소")
    private String detailAddress;

    @Schema(description = "현재 거주 형태")
    private PetApplicationForm.HousingType housingType;

    @Schema(description = "직업")
    private String job;

    @Schema(description = "이전 동물 양육 경험")
    private PetApplicationForm.YesNo experience;

    @Schema(description = "현재 다른 반려동물 존재 여부")
    private PetApplicationForm.YesNo hasOtherPets;

    @Schema(description = "동거인 성인 수")
    private int adultCount;

    @Schema(description = "동거인 자녀 수")
    private int childrenCount;

    @Schema(description = "동거인 동의")
    private PetApplicationForm.YesNo allConsent;

    @Schema(description = "동거인포함 알러지 여부")
    private PetApplicationForm.YesNo hasAllergy;

    @Schema(description = "입양 동물의 현재 상태 확인 사진 요구 동의")
    private PetApplicationForm.YesNo consentForCheck;

    @Schema(description = "입양신청이유")
    private String applicationReason;

    @Schema(description = "입양신청서 작성 시간")
    private LocalDateTime formCreatedAt;

    @Schema(description = "입양신청서 재전송 시간")
    private LocalDateTime resentAt;

    @Builder
    public MyPetApplicationDetailDTO(Long formId, LocalDateTime resentAt, LocalDateTime formCreatedAt, String noticeNo, String userName, LocalDate userBirth, PetApplicationForm.Gender userGender, String userPhone, String familyPhone, String family, String address, String detailAddress, PetApplicationForm.HousingType housingType, String job, PetApplicationForm.YesNo experience, PetApplicationForm.YesNo hasOtherPets, int adultCount, int childrenCount, PetApplicationForm.YesNo allConsent, PetApplicationForm.YesNo hasAllergy, PetApplicationForm.YesNo consentForCheck, String applicationReason) {
        this.formId = formId;
        this.noticeNo = noticeNo;
        this.userName = userName;
        this.userBirth = userBirth;
        this.userGender = userGender;
        this.userPhone = userPhone;
        this.familyPhone = familyPhone;
        this.family = family;
        this.address = address;
        this.detailAddress = detailAddress;
        this.housingType = housingType;
        this.job = job;
        this.experience = experience;
        this.hasOtherPets = hasOtherPets;
        this.adultCount = adultCount;
        this.childrenCount = childrenCount;
        this.allConsent = allConsent;
        this.hasAllergy = hasAllergy;
        this.consentForCheck = consentForCheck;
        this.applicationReason = applicationReason;
        this.formCreatedAt = formCreatedAt;
        this.resentAt = resentAt;
    }

    public static MyPetApplicationDetailDTO toDTO(PetApplicationForm form) {
        return MyPetApplicationDetailDTO.builder()
                .formId(form.getFormId())
                .noticeNo(form.getNoticeNo())
                .userName(form.getUserName())
                .userBirth(form.getUserBirth())
                .userGender(form.getUserGender())
                .userPhone(form.getUserPhone())
                .familyPhone(form.getFamilyPhone())
                .family(form.getFamily())
                .address(form.getAddress())
                .detailAddress(form.getDetailAddress())
                .housingType(form.getHousingType())
                .job(form.getJob())
                .experience(form.getExperience())
                .hasOtherPets(form.getHasOtherPets())
                .adultCount(form.getAdultCount())
                .childrenCount(form.getChildrenCount())
                .allConsent(form.getHasAllergy())
                .hasAllergy(form.getHasAllergy())
                .consentForCheck(form.getConsentForCheck())
                .applicationReason(form.getApplicationReason())
                .formCreatedAt(form.getFormCreatedAt())
                .build();
    }
}