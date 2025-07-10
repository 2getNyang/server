package com.project.nyang.modules.animal.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * AnimalDTO입니다.
 *
 * @author : 엄아영
 * @fileName : AnimalDTO
 * @since : 2025-07-09
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDTO {
    //유기 동물 번호
    private String desertionNo;
    //발견일
    private LocalDate happenDt;
    //발견 장소
    private String happenPlace;

    //털색, 무늬
    private String colorCd;
    //나이
    private String age;
    //무게
    private String weight;
    //공고 번호
    private String noticeNo;
    //공고 시작
    private LocalDate noticeSdt;
    //공고 종료
    private LocalDate noticeEdt;
    //이미지 1~3
    private String popfile1;
    private String popfile2;
    private String popfile3;
    //보호상태 (NOTICE / PROTECT / FINISH)
    private String processState;
    // 성별 (M / F / Q)
    private String sexCd;
    //중성화 여부 (Y / N / U)
    private String neuterYn;
    //특이 사항
    private String specialMark;
    //API 수정 시각
    private LocalDateTime updTm;

    //Shelter Entity
    //보호소 번호
    private String careRegNumber;

    //UpKind, Kind Entity
    //동물 종류 전체 이름 ex.[개] 시바
    private String kindFullNm;
    //축종코드
    private String upKindCd;
    //축종 이름 ex.개
    private String upKindNm;
    //품종 코드
    private String kindCd;
    //품종명 ex.시바
    private String kindNm;

}