package com.project.nyang.modules.animal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 동물 데이터를 매핑하는 AnimalApiResponse DTO클래스 입니다.
 *
 * @author : 엄아영
 * @fileName : AnimalApiResponse
 * @since : 2025-07-14
 */

@Data
@Schema(description = "공공데이터 유기동물 API 응답 DTO")
public class AnimalApiResponse {
    //유기 동물 번호
    @Schema(description = "유기동물 번호", example = "448534202501199")
    private String desertionNo;
    //발견일
    @Schema(description = "발견일", example = "20250714")
    private String happenDt;
    //발견 장소
    @Schema(description = "발견 장소", example = "서울시 종로구 혜화동")
    private String happenPlace;
    //동물 종류 전체 이름 ex.[개] 시바
    @Schema(description = "동물 전체 품종명 (예: [개] 시바)", example = "[개] 시바")
    private String kindFullNm;
    @Schema(description = "축종 코드", example = "417000")
    private String upKindCd;
    @Schema(description = "축종명", example = "개")
    private String upKindNm;
    @Schema(description = "품종 코드", example = "000054")
    private String kindCd;
    @Schema(description = "품종명", example = "시바")
    private String kindNm;
    //털색, 무늬
    @Schema(description = "털색/무늬", example = "갈색")
    private String colorCd;
    //나이
    @Schema(description = "나이", example = "2023(년생)")
    private String age;
    //무게
    @Schema(description = "무게", example = "5(Kg)")
    private String weight;
    //공고 번호
    @Schema(description = "공고 번호", example = "서울-종로1-2025-00123")
    private String noticeNo;
    //공고 시작
    @Schema(description = "공고 시작일", example = "20250715")
    private String noticeSdt;
    //공고 종료
    @Schema(description = "공고 종료일", example = "20250725")
    private String noticeEdt;
    //이미지 1
    @Schema(description = "대표 이미지1 URL", example = "http://example.com/image1.jpg")
    private String popfile1;
    //이미지 2
    @Schema(description = "대표 이미지2 URL", example = "http://example.com/image2.jpg")
    private String popfile2;
    //이미지 3
    @Schema(description = "대표 이미지3 URL", example = "http://example.com/image3.jpg")
    private String popfile3;
    //보호상태 (보호중 / 종료)
    @Schema(description = "보호상태", example = "보호중")
    private String processState;
    // 성별 (M / F / Q)
    @Schema(description = "성별 (M: 수컷, F: 암컷, Q: 미확인)", example = "M")
    private String sexCd;
    //중성화 여부 (Y / N / U)
    @Schema(description = "중성화 여부 (Y: 예, N: 아니오, U: 미상)", example = "N")
    private String neuterYn;
    //특이 사항
    @Schema(description = "특이사항", example = "왼쪽 다리에 상처 있음")
    private String specialMark;
    //보호소 번호
    @Schema(description = "보호소 등록번호", example = "611320202300001")
    private String careRegNo;
    //보호소 이름
    @Schema(description = "보호소 이름", example = "혜화유기동물보호센터")
    private String careNm;
    //보호소 전화번호
    @Schema(description = "보호소 전화번호", example = "02-123-4567")
    private String careTel;
    //보호소 주소
    @Schema(description = "보호소 주소", example = "서울특별시 종로구 혜화동 225")
    private String careAddr;
    //보호소 장 이름
    @Schema(description = "보호소 담당자명", example = "홍길동")
    private String careOwnerNm;
    //보호소 시도, 시군구 이름
    @Schema(description = "시도 + 시군구 이름", example = "서울특별시 종로구")
    private String orgNm;
    //API 수정 시각
    @Schema(description = "API 수정 시각 (yyyyMMddHHmmss)", example = "20250714010101")
    private String updTm;
}