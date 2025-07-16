package com.project.nyang.modules.animal.dto;

import lombok.Data;

/**
 * 동물 데이터를 매핑하는 AnimalApiResponse DTO클래스 입니다.
 *
 * @author : 엄아영
 * @fileName : AnimalApiResponse
 * @since : 2025-07-14
 */

@Data
public class AnimalApiResponse {
    //유기 동물 번호
    private String desertionNo;
    //발견일
    private String happenDt;
    //발견 장소
    private String happenPlace;
    //동물 종류 전체 이름 ex.[개] 시바
    private String kindFullNm;
    private String upKindCd;
    private String upKindNm;
    private String kindCd;
    private String kindNm;
    //털색, 무늬
    private String colorCd;
    //나이
    private String age;
    //무게
    private String weight;
    //공고 번호
    private String noticeNo;
    //공고 시작
    private String noticeSdt;
    //공고 종료
    private String noticeEdt;
    //이미지 1
    private String popfile1;
    //이미지 2
    private String popfile2;
    //이미지 3
    private String popfile3;
    //보호상태 (NOTICE / PROTECT / FINISH)
    private String processState;
    // 성별 (M / F / Q)
    private String sexCd;
    //중성화 여부 (Y / N / U)
    private String neuterYn;
    //특이 사항
    private String specialMark;
    //보호소 번호
    private String careRegNo;
    //보호소 이름
    private String careNm;
    //보호소 전화번호
    private String careTel;
    //보호소 주소
    private String careAddr;
    //보호소 장 이름
    private String careOwnerNm;
    //보호소 시도, 시군구 이름
    private String orgNm;
    //API 수정 시각
    private String updTm;
}