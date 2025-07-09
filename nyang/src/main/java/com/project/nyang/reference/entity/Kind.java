package com.project.nyang.reference.entity;

import com.project.nyang.modules.animal.entity.Animal;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 동물의 품종 DTO 입니다.
 *
 * @author : 선순주
 * @fileName : Kind
 * @since : 2025-07-09
 */
@Entity
@Table(name = "KIND")
@Getter
@NoArgsConstructor
public class Kind {

    @Id
    @Column(name = "kind_cd")
    private String kindCd;

    @Column(name = "kind_nm", nullable = false)
    private String kindNm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_kind_cd")
    private UpKind upKindCd;

    @OneToMany(mappedBy = "kind", cascade = CascadeType.ALL)
    private List<Animal> animals = new ArrayList<>();

    @Builder
    public Kind(String kindCd, String kindNm, UpKind upKindCd) {
        this.kindCd = kindCd;
        this.kindNm = kindNm;
        this.upKindCd = upKindCd;
    }
}