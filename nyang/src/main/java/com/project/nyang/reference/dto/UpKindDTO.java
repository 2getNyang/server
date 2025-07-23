package com.project.nyang.reference.dto;

import com.project.nyang.reference.entity.UpKind;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "축종 코드", example = "417000")
    private String upKindCd;
    @Schema(description = "축종 이름", example = "개")
    private String upKindName;

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