package com.project.nyang.modules.animal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 메인페이지용 DTO입니다.
 *
 * @author : 선순주
 * @fileName : AnimalDashboardDTO
 * @since : 2025-07-19
 */

@Getter
@Builder
@AllArgsConstructor
public class AnimalDashboardDTO {
    private long shelterCount;
    private long protectedAnimalCount;
    private long adoptedOrReturnedCount;
}