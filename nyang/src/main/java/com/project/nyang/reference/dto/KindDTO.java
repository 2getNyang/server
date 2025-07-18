package com.project.nyang.reference.dto;

import com.project.nyang.reference.entity.Kind;
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
    private String kindCd;     // 예: "000054"
    private String kindName;   // 예: "푸들"

    public static KindDTO from(Kind kind) {
        return KindDTO.builder()
                .kindCd(kind.getKindCd())
                .kindName(kind.getKindNm())
                .build();
    }
}