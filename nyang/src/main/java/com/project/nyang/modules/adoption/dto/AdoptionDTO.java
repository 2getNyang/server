package com.project.nyang.modules.adoption.dto;

import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * AdoptionDTO 
 * @fileName        : AdoptionDTO
 * @author          : 이지은
 * @since           : 25. 7. 14.
 * 
 */
@Getter
@Builder
public class AdoptionDTO {

    @Schema(description = "입양신청서 id")
    private Long formId;

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

    @Schema(description = "입양신청 동물 id")
    private String desertionNo;
    @Schema(description = "입양신청 동물의 보호소 id")
    private String careRegNumber;
    @Schema(description = "입양신청자 id")
    private Long userId;

}