package com.project.nyang.modules.board.elasticsearch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 입양후기, sns 홍보 게시판 리스트 DTO
 *
 * @author : 박세정
 * @fileName : BoardListDTO
 * @since : 2025-07-15
 */
@Getter
public class BoardListDTO {
    @Schema(description = "게시글 ID")
    private String id;
    @Schema(description = "조회수")
    private Long boardViewCount;
    @Schema(description = "카테고리 ID")
    private Long categoryId;
    @Schema(description = "게시글 제목")
    private String boardTitle;
    @Schema(description = "게시글 내용")
    private String boardContent;
    @Schema(description = "생성일자")
    private LocalDateTime createdAt;
    @Schema(description = "대표 이미지 url")
    private String imageUrl;

    @Builder
    public BoardListDTO(String id, Long boardViewCount, Long categoryId, String boardTitle, String boardContent, LocalDateTime createdAt, String imageUrl) {
        this.id = id;
        this.boardViewCount = boardViewCount;
        this.categoryId = categoryId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }
}