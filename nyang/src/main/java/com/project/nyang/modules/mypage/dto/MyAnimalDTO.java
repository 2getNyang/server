package com.project.nyang.modules.mypage.dto;

import com.project.nyang.modules.animal.entity.Animal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 내가 찜한 입양공고 리스트 DTO
 *
 * @author : 박세정
 * @fileName : MyAnimalDTO
 * @since : 2025-07-12
 */
@Getter
public class MyAnimalDTO {
    @Schema(description = "입양공고 ID")
    private String desertionNo;
    @Schema(description = "입양신청 상태")
    private String processState;
    @Schema(description = "성별")
    private String sexCd;
    @Schema(description = "동물 이름")
    private String kindFullNm;
    @Schema(description = "공고 번호")
    private String noticeNo;
    @Schema(description = "발생일자")
    private LocalDate happenDt;
    @Schema(description = "실종장소")
    private String happenPlace;
    @Schema(description = "사진")
    private String popfile1;

    @Builder
    public MyAnimalDTO(String desertionNo, String processState, String sexCd, String kindFullNm, String noticeNo, LocalDate happenDt, String happenPlace, String popfile1) {
        this.desertionNo = desertionNo;
        this.processState = processState;
        this.sexCd = sexCd;
        this.kindFullNm = kindFullNm;
        this.noticeNo = noticeNo;
        this.happenDt = happenDt;
        this.happenPlace = happenPlace;
        this.popfile1 = popfile1;
    }

    public static MyAnimalDTO of(Animal animal) {
        return MyAnimalDTO.builder()
                .desertionNo(animal.getDesertionNo())
                .processState(animal.getProcessState())
                .sexCd(animal.getSexCd())
                .kindFullNm(animal.getKindFullNm())
                .noticeNo(animal.getNoticeNo())
                .happenDt(animal.getHappenDt())
                .happenPlace(animal.getHappenPlace())
                .popfile1(animal.getPopfile1())
                .build();
    }
}