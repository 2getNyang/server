package com.project.nyang.modules.shelter.dto;

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
    private String careNm;        // 보호소 이름
    private String careRegNo;     // 보호소 등록 번호
    private String careAddr;      // 보호소 도로명 주소
    private String jibunAddr;     // 보호소 지번 주소
    private Double lat;           // 위도
    private Double lng;           // 경도
    private String careTel;       // 보호소 전화번호
    private String orgNm;         // 관할 지자체 이름 (시/도 + 시/군/구)
    private String dataStdDt;     // 데이터 기준일자
}