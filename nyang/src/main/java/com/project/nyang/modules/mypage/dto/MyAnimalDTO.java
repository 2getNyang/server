package com.project.nyang.modules.mypage.dto;

import com.project.nyang.modules.animal.entity.Animal;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 내가 찜한 입양공고 리스트 DTO
 *
 * @author : 박세정
 * @fileName : MyAnimalDTO
 * @since : 2025-07-12
 */
@Getter
public class MyAnimalDTO {
    private String desertionNo;
    private String processState;
    private String sexCd;
    private String kindFullNm;
    private String noticeNo;
    private LocalDate happenDt;
    private String happenPlace;
    private String popfile1;

    @Builder
    public MyAnimalDTO(String desertionNo, String processState, String sexCd, String kindFullNm, String noticeNo, LocalDate happenDt, String happenPlace, String popfile1) {
        this.desertionNo = desertionNo;
        this.processState = processState;
        this.sexCd = sexCd;
        this.kindFullNm = kindFullNm;
        this.noticeNo = noticeNo;
        this.happenDt = happenDt;
        this.happenPlace = happenPlace;
        this.popfile1 = popfile1;
    }

    public static MyAnimalDTO of(Animal animal) {
        return MyAnimalDTO.builder()
                .desertionNo(animal.getDesertionNo())
                .processState(animal.getProcessState())
                .sexCd(animal.getSexCd())
                .kindFullNm(animal.getKindFullNm())
                .noticeNo(animal.getNoticeNo())
                .happenDt(animal.getHappenDt())
                .happenPlace(animal.getHappenPlace())
                .popfile1(animal.getPopfile1())
                .build();
    }
}