package com.project.nyang.modules.animal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * AnimalListDTO
 * 전체 동물 조회에 쓰이는 DTO입니다.
 *
 * @author : 엄아영
 * @fileName : AnimalListDTO
 * @since : 2025-07-09
 */

@Data
@AllArgsConstructor
public class AnimalListDTO{
    private String desertionNo;
    private String processState;
    private String sexCd;
    private String kindFullNm;
    private String noticeNo;
    private LocalDate happenDt;
    private String happenPlace;
    private String popfile1;
}