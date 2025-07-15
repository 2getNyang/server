package com.project.nyang.modules.shelter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ShelterDetailDTO에 대한 클래스입니다
 * 보호소 상세 조회 응답 DTO
 *
 * @author : 오승훈
 * @fileName : ShelterDetailDTO
 * @since : 2025-07-14
 */
@Data
@AllArgsConstructor
public class ShelterDetailDTO {
    @Schema(description = "보호소 등록 번호", example = "서울 동물보호소")
    private String careRegNumber;

    @Schema(description = "보호소 이름", example = "서울 동물보호소")
    private String careName;

    @Schema(description = "보호소 전화번호", example = "02-1234-5678")
    private String careTel;

    @Schema(description = "도로명 주소", example = "서울특별시 용산구 원효로2가 84-10")
    private String careAddress;

    @Schema(description = "위도", example = "37.8705")
    private Float latitude;

    @Schema(description = "경도", example = "126.984")
    private Float longitude;

    @Schema(description = "시/도 이름", example = "서울특별시")
    private String regionName;

    @Schema(description = "시/군/구 이름", example = "강남구")
    private String subRegionName;
}