package com.project.nyang.modules.board.lost.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 실종/목격 제보 게시판 특정 글 조회 DTO입니다.
 *
 * @author : 선순주
 * @fileName : LostDetailResponseDTO
 * @since : 2025-07-09
 */
@Getter
@Setter
@Builder
public class LostDetailResponseDTO {
    @Schema(description = "게시글 ID", example = "12")
    private Long boardId;
    @Schema(description = "카테고리 ID", example = "4")
    private Long categoryId;
    @Schema(description = "유저 ID", example = "1")
    private Long userId;
    @Schema(description = "유저닉네임", example = "고양이 대장")
    private String nickName;

    @Schema(description = "게시글 타입. (MS:실종 / WT:목격)", example = "MS")
    private String lostType;    //MS : 실종 or WT : 목격
    @Schema(description = "품종", example = "레그돌")
    private String kindName;
    @Schema(description = "나이", example = "3")
    private Integer age;
    @Schema(description = "털 색깔", example = "갈색")
    private String furColor;
    @Schema(description = "성별(M:수컷 / F:암컷 / Q:모름)", example = "F")
    private String gender;      // M:수컷 / F : 암컷 / Q : 모름

    @Schema(description = "시/도 이름", example = "경기도")
    private String regionName;
    @Schema(description = "시/군/구 이름", example = "부천시")
    private String subRegionName;
    @Schema(description = "실종/목격 날짜", example = "2025-07-12")
    private LocalDate missingDate;
    @Schema(description = "실종/목격 장소", example = "부천역 3번 출구 근처")
    private String missingLocation;
    @Schema(description = "작성자 연락처", example = "010-2222-3333")
    private String phone;

    @Schema(description = "이미지 S3 url 리스트", example = "[\"https://s3.amazonaws.com/bucket/image1.jpg\", \"https://s3.amazonaws.com/bucket/image2.jpg\"]")
    private List<String> imageUrls;
    @Schema(description = "생성일자", example = "2025-07-13 14:30:00")
    private LocalDateTime createdAt;
    @Schema(description = "삭제일자", example = "2025-07-13 14:30:00")
    private LocalDateTime deletedAt;
}