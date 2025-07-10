package com.project.nyang.modules.animal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author : 이지은
 * @fileName : AnimalDTO
 * @since : 25. 7. 9.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDTO {

    private String desertionNo;//동물번호
    private LocalDate happenDt; //발견일자
    private String happenPlace; //발견장소

    private String colorCd;
    private String age;
    private String weight;
    private String noticeNo; //공고번호
    private LocalDate noticeSdt; //공고시작시간
    private LocalDate noticeEdt; //공고종료시간

    //동물 이미지
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