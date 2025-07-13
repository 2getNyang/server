package com.project.nyang.modules.shelter.entity;

import com.project.nyang.global.common.entity.BaseTime;
import com.project.nyang.reference.entity.Region;
import com.project.nyang.reference.entity.SubRegion;
import jakarta.persistence.*;
import lombok.*;

/**
 *
 * shelter 엔티티 클래스입니다.
 * @fileName        : Shelter
 * @author          : 오승훈
 * @since           : 2025-07-07
 *
 */
@Entity
@Getter
@NoArgsConstructor
public class Shelter {
    @Id
    @Column(name = "care_reg_number", nullable = false)
    private String careRegNumber;

    @Column(name = "care_name")
    private String careName;

    @Column(name = "care_address")
    private String careAddress;

    @Column(name = "jibun_address")
    private String jibunAddress;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "care_tel")
    private String careTel;

    @Column(name = "care_email")
    private String careEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_code", nullable = false)
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_region_code", nullable = false)
    private SubRegion subRegion;

    @Builder
    public Shelter(String careRegNumber, String careName, String careAddress,
                   String jibunAddress, Float latitude, Float longitude,
                   String careTel, String careEmail,
                   Region region, SubRegion subRegion) {
        this.careRegNumber = careRegNumber;
        this.careName = careName;
        this.careAddress = careAddress;
        this.jibunAddress = jibunAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.careTel = careTel;
        this.careEmail = careEmail;
        this.region = region;
        this.subRegion = subRegion;
    }
}
