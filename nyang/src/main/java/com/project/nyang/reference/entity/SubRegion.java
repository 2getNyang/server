package com.project.nyang.reference.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 *
 * subregion 엔티티 클래스입니다.
 * @fileName        : SubRegion
 * @author          : 오승훈
 * @since           : 2025-07-07
 *
 */

@Entity
@Getter
@NoArgsConstructor
@Table(name = "SUB_REGION")
public class SubRegion {
    @Id
    @Column(name = "sub_region_code", nullable = false)
    private String subRegionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_code", nullable = false)
    private Region region;

    @Column(name = "sub_region_name")
    private String subRegionName;

    @Builder
    public SubRegion(String subRegionCode, Region region, String subRegionName) {
        this.subRegionCode = subRegionCode;
        this.region = region;
        this.subRegionName = subRegionName;
    }
}
