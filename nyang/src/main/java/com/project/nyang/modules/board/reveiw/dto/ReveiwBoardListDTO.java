package com.project.nyang.modules.board.reveiw.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 입양 후기 게시판 리스트 요청 response DTO
 *
 * @author : 박세정
 * @fileName : ReveiwBoardListDTO
 * @since : 2025-07-09
 */
public class ReveiwBoardListDTO {
    @Schema(description = "사용자 닉네임")
    private String nickname;
    @Schema(description = "게시글 제목")
    private String boardTitle;
    @Schema(description = "게시글 내용")
    private String boardContent;
    @Schema(description = "첫번째 사진 url")
    private String imageUrl;
    @Schema(description = "생성일자")
    private LocalDateTime createdAt;
    @Schema(description = "조회수")
    private Long boardViewCount;
    @Schema(description = "좋아요 수")
    private int likeItCount;
    @Schema(description = "좋아요 여부")
    private Boolean isLiked;

    @Builder
    public ReveiwBoardListDTO(String nickname, String boardTitle, String boardContent, String imageUrl, LocalDateTime createdAt, Long boardViewCount, int likeItCount, int commentCount, Boolean isLiked) {
        this.nickname = nickname;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.boardViewCount = boardViewCount;
        this.likeItCount = likeItCount;
        this.isLiked = isLiked;
    }
}