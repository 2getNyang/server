package com.project.nyang.global.elasticsearch.animal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Elasticsearch에 저장되는 공공데이터 API 동물 정보 모델
 *
 * @author : 엄아영
 * @fileName : AnimalEsDocument
 * @since : 2025-07-17
 */
@JsonIgnoreProperties(ignoreUnknown = true) // 해당 설정을 넣지 않으면 class 속성이 들어가게 됨
@Document(indexName = "animal-index")
@Getter
@NoArgsConstructor
public class AnimalEsDocument {
    @Id
    private String desertionNo;

    @Schema(description = "보호상태 (NOTICE / PROTECT / FINISH)", example = "NOTICE")
    private String processState;

    @Schema(description = "성별 (M / F / Q)", example = "F")
    private String sexCd;

    @Schema(description = "털색, 무늬")
    private String colorCd;

    @Schema(description = "나이")
    private String age;

    @Schema(description = "무게")
    private String weight;

    @Schema(description = "특이 사항", example = "산책을 좋아함")
    private String specialMark;

    @Schema(description = "동물 종류 전체 이름", example = "[고양이] 샴")
    private String kindFullNm;

    @Schema(description = "공고 번호", example = "서울특별시-성동구-2025-00535")
    private String noticeNo;

    @Schema(description = "발견일", example = "2025-07-18")
    private String happenDt;

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

    @Schema(description = "보호소 번호")
    private String careRegNumber;

    @Schema(description = "보호소 이름")
    private String careName;

    @Builder
    public AnimalEsDocument(String desertionNo, String processState, String sexCd, String colorCd, String age, String weight, String specialMark, String kindFullNm, String noticeNo, String happenDt, String happenPlace, String popfile1, String upKindCd, String upKindNm, String kindCd, String kindNm, String regionCode, String regionName, String subRegionCode, String subRegionName,  String careRegNumber, String careName) {
        this.desertionNo = desertionNo;
        this.processState = processState;
        this.sexCd = sexCd;
        this.colorCd = colorCd;
        this.age = age;
        this.weight = weight;
        this.specialMark = specialMark;
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
        this.careRegNumber = careRegNumber;
        this.careName = careName;
    }

    public static AnimalEsListDTO toAnimalEsDTO(AnimalEsDocument document) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return AnimalEsListDTO.builder()
                .desertionNo(document.getDesertionNo())
                .processState(document.getProcessState())
                .sexCd(document.getSexCd())
                .kindFullNm(document.getKindFullNm())
                .noticeNo(document.getNoticeNo())
                .happenDt(LocalDate.parse(document.getHappenDt(), dateTimeFormatter))
                .happenPlace(document.getHappenPlace())
                .popfile1(document.getPopfile1())
                .upKindCd(document.getUpKindCd())
                .upKindNm(document.getUpKindNm())
                .kindCd(document.getKindCd())
                .kindNm(document.getKindNm())
                .regionCode(document.getRegionCode())
                .regionName(document.getRegionName())
                .subRegionCode(document.getSubRegionCode())
                .subRegionName(document.getSubRegionName())
                .build();
    }


}