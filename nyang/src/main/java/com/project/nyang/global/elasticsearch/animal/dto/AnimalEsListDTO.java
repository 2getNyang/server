package com.project.nyang.global.elasticsearch.animal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * AnimalEsListDTO
 * 전체 동물 조회에 쓰이는 DTO입니다.
 *
 * @author : 엄아영
 * @fileName : AnimalEsListDTO
 * @since : 2025-07-17
 */

@Getter
@SuperBuilder
public class AnimalEsListDTO {
    @Schema(description = "유기 동물 번호", example = "311303202500535")
    private String desertionNo;

    @Schema(description = "보호상태 (NOTICE / PROTECT / FINISH)", example = "NOTICE")
    private String processState;

    @Schema(description = "성별 (M / F / Q)", example = "F")
    private String sexCd;

    @Schema(description = "동물 종류 전체 이름", example = "[고양이] 샴")
    private String kindFullNm;

    @Schema(description = "공고 번호", example = "서울특별시-성동구-2025-00535")
    private String noticeNo;

    @Schema(description = "발견일", example = "2025-07-18")
    private LocalDate happenDt;

    @Schema(description = "발견 장소", example = "서울특별시 성동구")
    private String happenPlace;

    @Schema(description = "이미지1", example = "http://example.com/311303202500535.jpg")
    private String popfile1;

    @Schema(description = "축종 코드", example = "417000")
    private String upKindCd;

    @Schema(description = "축종 이름", example = "개")
    private String upKindNm;

    @Schema(description = "품종 코드", example = "000114")
    private String kindCd;

    @Schema(description = "품종 이름", example = "시바")
    private String kindNm;

    @Schema(description = "시도 코드", example = "6110000")
    private String regionCode;

    @Schema(description = "시도 이름", example = "서울특별시")
    private String regionName;

    @Schema(description = "시군구 코드", example = "3010000")
    private String subRegionCode;

    @Schema(description = "시군구 이름", example = "중구")
    private String subRegionName;


    public AnimalEsListDTO(String desertionNo, String processState, String sexCd, String kindFullNm, String noticeNo, LocalDate happenDt, String happenPlace, String popfile1, String upKindCd, String upKindNm, String kindCd, String kindNm, String regionCode, String regionName, String subRegionCode, String subRegionName) {
        this.desertionNo = desertionNo;
        this.processState = processState;
        this.sexCd = sexCd;
        this.kindFullNm = kindFullNm;
        this.noticeNo = noticeNo;
        this.happenDt = happenDt;
        this.happenPlace = happenPlace;
        this.popfile1 = popfile1;
        this.upKindCd = upKindCd;
        this.upKindNm = upKindNm;
        this.kindCd = kindCd;
        this.kindNm = kindNm;
        this.regionCode = regionCode;
        this.regionName = regionName;
        this.subRegionCode = subRegionCode;
        this.subRegionName = subRegionName;
    }
}