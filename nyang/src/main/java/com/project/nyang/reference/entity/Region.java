package com.project.nyang.reference.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * region 엔티티 클래스입니다.
 * @fileName        : Region
 * @author          : 오승훈
 * @since           : 2025-07-07
 *
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    @Id
    @Column(name = "region_code", nullable = false)
    private String regionCode;

    @Column(name = "region_name", nullable = false)
    private String regionName;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    private List<SubRegion> subRegions = new ArrayList<>();

    @Builder
    public Region(String regionCode, String regionName) {
        this.regionCode = regionCode;
        this.regionName = regionName;
    }

}
