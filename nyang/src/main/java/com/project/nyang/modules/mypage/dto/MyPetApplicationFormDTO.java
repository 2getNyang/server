package com.project.nyang.modules.mypage.dto;

import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 입양신청내역 DTO
 *
 * @author : 박세정
 * @fileName : MyPetApplicationFormDTO
 * @since : 2025-07-12
 */
@Getter
public class MyPetApplicationFormDTO {
    @Schema(description = "입양 신청 ID")
    private Long formId;
    @Schema(description = "입양 신청 ID")
    private String processState;
    private String sexCd;
    private String kindFullNm;
    private String noticeNo;
    private String happenPlace;
    private String popfile1;
    private LocalDateTime notyCreatedAt;

    @Builder
    public MyPetApplicationFormDTO(Long formId, String processState, String sexCd, String kindFullNm, String noticeNo, String happenPlace, String popfile1, LocalDateTime notyCreatedAt) {
        this.formId = formId;
        this.processState = processState;
        this.sexCd = sexCd;
        this.kindFullNm = kindFullNm;
        this.noticeNo = noticeNo;
        this.happenPlace = happenPlace;
        this.popfile1 = popfile1;
        this.notyCreatedAt = notyCreatedAt;
    }

    public static MyPetApplicationFormDTO of(PetApplicationForm form) {
        return MyPetApplicationFormDTO.builder()
                .formId(form.getId())
                .processState(form.getAnimal().getProcessState())
                .sexCd(form.getAnimal().getSexCd())
                .kindFullNm(form.getAnimal().getKindFullNm())
                .noticeNo(form.getAnimal().getNoticeNo())
                .happenPlace(form.getAnimal().getHappenPlace())
                .popfile1(form.getAnimal().getPopfile1())
                .notyCreatedAt(LocalDateTime.now())
                .build();
    }
}