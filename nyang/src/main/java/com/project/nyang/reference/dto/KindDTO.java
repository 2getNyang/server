package com.project.nyang.reference.dto;

import com.project.nyang.reference.entity.Kind;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 품종 DTO
 *
 * @author : 선순주
 * @fileName : KindDTO
 * @since : 2025-07-17
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KindDTO {
    @Schema(description = "품종 코드", example = "000001")
    private String kindCd;     // 예: "000054"
    @Schema(description = "품종 이름", example = "삽살개")
    private String kindName;   // 예: "푸들"

    public KindDTO(String kindName) {this.kindName = kindName;}

    public static KindDTO from(Kind kind) {
        return KindDTO.builder()
                .kindCd(kind.getKindCd())
                .kindName(kind.getKindNm())
                .build();
    }
}