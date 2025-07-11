package com.project.nyang.modules.board.lost.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 실종/목격 제보 게시판 전체 글 조회 DTO입니다.
 *
 * @author : 선순주
 * @fileName : LostListResponseDTO
 * @since : 2025-07-09
 */
@Getter
@Setter
@Builder
public class LostListResponseDTO {
    private Long boardId;
    private Long categoryId;
    private Long userId;


    private String lostType;    //MS : 실종 or WT : 목격
    private Long viewCount;
    private String kindName;    //품종
    private Integer age;
    private String furColor;
    private String gender;  // M:수컷 / F : 암컷 / Q : 모름

    private String missingLocation;
    private LocalDate missingDate;
    private String thumbnailUrl;

    private LocalDateTime createdAt;
}