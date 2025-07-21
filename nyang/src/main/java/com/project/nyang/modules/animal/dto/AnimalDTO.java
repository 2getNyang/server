package com.project.nyang.modules.animal.dto;

import com.project.nyang.modules.comment.dto.AnimalCommentDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * AnimalDTO 상세조회에 사용합니다.
 *
 * @author : 이지은, 엄아영
 * @fileName : AnimalDTO
 * @since : 25. 7. 9.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimalDTO {
    @Schema(description = "유기 동물 번호", example = "311303202500535")
    private String desertionNo;

    @Schema(description = "발견일", example = "2025-07-18")
    private LocalDate happenDt;

    @Schema(description = "발견 장소", example = "서울특별시 성동구")
    private String happenPlace;

    @Schema(description = "털색, 무늬")
    private String colorCd;

    @Schema(description = "나이")
    private String age;

    @Schema(description = "무게")
    private String weight;

    @Schema(description = "공고 번호", example = "서울특별시-성동구-2025-00535")
    private String noticeNo;

    @Schema(description = "공고 시작")
    private LocalDate noticeSdt;

    @Schema(description = "공고 종료")
    private LocalDate noticeEdt;

    @Schema(description = "이미지 1", example = "")
    private String popfile1;

    @Schema(description = "이미지 2", example = "")
    private String popfile2;

    @Schema(description = "이미지 3", example = "")
    private String popfile3;

    @Schema(description = "보호상태 (NOTICE / PROTECT / FINISH)", example = "NOTICE")
    private String processState;

    @Schema(description = "성별 (M / F / Q)", example = "M")
    private String sexCd;

    @Schema(description = "중성화 여부 (Y / N / U)", example = "N")
    private String neuterYn;

    @Schema(description = "특이 사항", example = "산책을 좋아함")
    private String specialMark;

    @Schema(description = "API 수정 시각")
    private LocalDateTime updTm;

    @Schema(description = "동물 종류 전체 이름", example = "[고양이] 샴")
    private String kindFullNm;

    @Schema(description = "축종코드", example = "")
    private String upKindCd;

    @Schema(description = "축종 이름", example = "개")
    private String upKindNm;

    @Schema(description = "품종 코드", example = "000114")
    private String kindCd;

    @Schema(description = "품종명", example = "시바")
    private String kindNm;

    @Schema(description = "댓글 리스트")
    private List<AnimalCommentDTO> comments;
    
    @Schema(description = "북마크 체크")
    private boolean bookmarked;

    //보호소 정보 추가
    @Schema(description = "보호소 이름", example = "성동구 유기동물보호소")
    private String shelterName;

    @Schema(description = "보호소 주소", example = "서울특별시 성동구 행당로 17")
    private String shelterAddress;

    @Schema(description = "보호소 전화번호", example = "02-1234-5678")
    private String shelterTel;

    @Schema(description = "보호소ID", example = "348527200900001")
    private String careRegNumber;



}