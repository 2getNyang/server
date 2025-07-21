package com.project.nyang.reference.dto;

import com.project.nyang.reference.entity.UpKind;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 축종 DTO
 *
 * @author : 선순주
 * @fileName : UpKindDTO
 * @since : 2025-07-17
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpKindDTO {
    private String upKindCd;     // 예: "417000"
    private String upKindName;   // 예: "고양이"

    public UpKindDTO(String upKindName) {
        this.upKindName = upKindName;
    }

    public static UpKindDTO from(UpKind upKind) {
        return UpKindDTO.builder()
                .upKindCd(upKind.getUpKindCd())
                .upKindName(upKind.getUpKindNm())
                .build();
    }
}