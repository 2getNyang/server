package com.project.nyang.modules.shelter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 보호소 데이터를 매핑하는 ShelterApiResponse DTO 클래스 입니다.
 *
 * @author : 오승훈
 * @fileName : ShelterApiResponse
 * @since : 2025-07-16
 */
@Data
public class ShelterApiResponse {
    @Schema(description = "보호소 이름", example = "서울 동물보호소")
    private String careNm;        // 보호소 이름
    @Schema(description = "보호소 등록 번호", example = "서울 311300201300001")
    private String careRegNo;     // 보호소 등록 번호
    @Schema(description = "도로명 주소", example = "서울특별시 용산구 원효로2가 84-10")
    private String careAddr;      // 보호소 도로명 주소
    @Schema(description = "지번 주소")
    private String jibunAddr;     // 보호소 지번 주소
    @Schema(description = "위도", example = "37.8705")
    private Double lat;           // 위도
    @Schema(description = "경도", example = "126.984")
    private Double lng;           // 경도
    @Schema(description = "보호소 전화번호", example = "02-1234-5678")
    private String careTel;       // 보호소 전화번호
    @Schema(description = "관할 지자체 이름 (시/도 + 시/군/구)", example = "서울특별시 종로구")
    private String orgNm;         // 관할 지자체 이름 (시/도 + 시/군/구)
    @Schema(description = "데이터 기준일자")
    private String dataStdDt;     // 데이터 기준일자
}