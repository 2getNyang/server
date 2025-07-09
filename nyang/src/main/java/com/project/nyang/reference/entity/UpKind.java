package com.project.nyang.reference.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 동물의 축종 구분 엔티티입니다.
 *
 * @author : 선순주
 * @fileName : Upkind
 * @since : 2025-07-09
 */

@Entity
@Table(name = "UP_KIND")
@Getter
@NoArgsConstructor
public class UpKind {

    @Id
    @Column(name = "up_kind_cd")
    private String upKindCd;

    @Column(name = "up_kind_nm", nullable = false)
    private String upKindNm;

    @OneToMany(mappedBy = "upKind", cascade = CascadeType.ALL)
    private List<Kind> kinds = new ArrayList<>();

    @Builder
    public UpKind(String upKindCd, String upKindNm) {
        this.upKindCd = upKindCd;
        this.upKindNm = upKindNm;
    }
}