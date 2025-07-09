package com.project.nyang.modules.board.reveiw.dto;

import com.project.nyang.modules.comment.entity.Comment;
import com.project.nyang.modules.image.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 입양 후기 게시판 리스트 요청 response DTO
 *
 * @author : 박세정
 * @fileName : ReveiwBoardListDTO
 * @since : 2025-07-09
 */
public class ReveiwBoardDetailDTO {
    @Schema(description = "사용자 닉네임")
    private String nickname;
    @Schema(description = "사용자 아이디")
    private Long userId;
    @Schema(description = "게시글 제목")
    private String boardTitle;
    @Schema(description = "게시글 내용")
    private String boardContent;
    @Schema(description = "이미지 객체 배열")
    private List<Image> images;
    @Schema(description = "댓글 객체 배열")
    private List<Comment> comments;
    @Schema(description = "생성 시간")
    private LocalDateTime createdAt;
    @Schema(description = "조회수")
    private Long boardViewCount;
    @Schema(description = "좋아요 수")
    private int likeItCount;
    @Schema(description = "좋아요 여부")
    private Boolean isLiked;

    // =========== 신청서 내용 ===========
    @Schema(description = "신청 번호")
    private Long formId;
    @Schema(description = "동물 전체 이름")
    private String kindFullNm;
    @Schema(description = "나이")
    private String age;
    @Schema(description = "성별")
    private String sexCd;
    @Schema(description = "구조일")
    private LocalDate happenDt;
    @Schema(description = "지역 이름")
    private String subRegionName;
    @Schema(description = "보호소 이름")
    private String careName;

    @Builder(toBuilder = true)
    public ReveiwBoardDetailDTO(String nickname, Long userId, String boardTitle, String boardContent, List<Image> images, LocalDateTime createdAt, Long boardViewCount, int likeItCount, Boolean isLiked, Long formId, String kindFullNm, String age, String sexCd, LocalDate happenDt, String subRegionName, String careName, List<Comment> comments) {
        this.nickname = nickname;
        this.userId = userId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.images = images;
        this.createdAt = createdAt;
        this.boardViewCount = boardViewCount;
        this.likeItCount = likeItCount;
        this.isLiked = isLiked;
        this.formId = formId;
        this.kindFullNm = kindFullNm;
        this.age = age;
        this.sexCd = sexCd;
        this.happenDt = happenDt;
        this.subRegionName = subRegionName;
        this.careName = careName;
        this.comments = comments;
    }
}