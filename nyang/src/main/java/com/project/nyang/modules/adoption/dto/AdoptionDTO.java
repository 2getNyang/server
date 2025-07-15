package com.project.nyang.modules.adoption.dto;

import com.project.nyang.modules.adoption.entity.PetApplicationForm;
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
    private Long id;

    private String userName;
    private LocalDate userBirth;
    private PetApplicationForm.Gender userGender;
    private String userPhone;
    private String familyPhone;
    private String family;
    private String address;
    private String detailAddress;
    private String job;
    private PetApplicationForm.YesNo experience;
    private LocalDateTime formCreatedAt;
    private String applicationReason;

    private String desertionNo;   // Animal ID 대신 번호만
    private String careRegNumber; // Shelter ID 대신 등록번호만
    private Long userId;          // User ID

}