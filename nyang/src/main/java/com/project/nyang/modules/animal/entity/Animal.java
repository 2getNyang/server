package com.project.nyang.modules.animal.entity;

import com.project.nyang.global.common.entity.BaseTime;
import com.project.nyang.modules.shelter.entity.Shelter;
import com.project.nyang.reference.entity.Kind;
import com.project.nyang.reference.entity.SubRegion;
import com.project.nyang.reference.entity.UpKind;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Animal Entitiy입니다.
 * 스키마 검증을 위해 Column 어노테이션 옵션에 length를 설정하였습니다.
 *
 * @author : 엄아영
 * @fileName : Animal
 * @since : 2025-07-07
 */

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ANIMAL")
@Getter
@Builder
@Entity
public class Animal extends BaseTime {
    //유기 동물 번호
    @Id
    @Column(name = "desertion_no", length = 15)
    private String desertionNo;

    //발견일
    @Column(name = "happen_dt", nullable = false)
    private LocalDate happenDt;

    //발견 장소
    @Column(name = "happen_place", length = 100)
    private String happenPlace;

    //동물 종류 전체 이름 ex.[개] 시바
    @Column(name = "kind_full_nm", length = 50)
    private String kindFullNm;

    //축종코드
    @Column(name = "up_kind_cd", length = 6, nullable = false)
    private String upKindCd;

    //축종 이름 ex.개
    @Column(name = "up_kind_nm", length = 10)
    private String upKindNm;

    //품종 코드
    @Column(name = "kind_cd", length = 6, nullable = false)
    private String kindCd;

    //품종명 ex.시바
    @Column(name = "kind_nm", length = 40)
    private String kindNm;

    //털색, 무늬
    @Column(name = "color_cd", length = 40)
    private String colorCd;

    //나이
    @Column(length = 15)
    private String age;

    //무게
    @Column(name = "weight", length = 10)
    private String weight;

    //공고 번호
    @Column(name = "notice_no", length = 30)
    private String noticeNo;

    //공고 시작
    @Column(name = "notice_sdt")
    private LocalDate noticeSdt;

    //공고 종료
    @Column(name = "notice_edt")
    private LocalDate noticeEdt;

    //이미지 1~3
    @Column(name = "popfile1", length = 255)
    private String popfile1;

    @Column(name = "popfile2", length = 255)
    private String popfile2;

    @Column(name = "popfile3", length = 255)
    private String popfile3;

    //보호상태 (NOTICE / PROTECT / FINISH)
    @Column(name = "process_state", length = 10, nullable = false)
    private String processState;

    // 성별 (M / F / Q)
    @Column(name = "sex_cd", length = 1, nullable = false)
    private String sexCd;

    //중성화 여부 (Y / N / U)
    @Column(name = "neuter_yn", length = 1, nullable = false)
    private String neuterYn;

    //특이 사항
    @Column(name = "special_mark", length = 255)
    private String specialMark;

    //API 수정 시각
    @Column(name = "upd_tm")
    private LocalDateTime updTm;

    // 다 대 일
    //동물 한 마리는 보호소 한 개에 소속
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_reg_number", nullable = false)
    private Shelter shelter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_kind_cd", insertable = false, updatable = false)
    private UpKind upKind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kind_cd", insertable = false, updatable = false)
    private Kind kind;

}