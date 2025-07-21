package com.project.nyang.global.elasticsearch.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 입양후기, sns 홍보 게시판 리스트 DTO
 *
 * @author : 박세정
 * @fileName : BoardListDTO
 * @since : 2025-07-15
 */
@Getter
@SuperBuilder
public class BoardListDTO {
    @Schema(description = "게시글 ID")
    private String id;
    @Schema(description = "조회수")
    private Long viewCount;
    @Schema(description = "카테고리 ID")
    private Long categoryId;
    @Schema(description = "게시글 제목")
    private String boardTitle;
    @Schema(description = "게시글 내용")
    private String boardContent;
    @Schema(description = "생성일자")
    private String createdAt;
    @Schema(description = "대표 이미지 url")
    private String imageUrl;
    @Schema(description = "닉네임")
    private String nickname;

    public BoardListDTO(String id, Long viewCount, Long categoryId, String boardTitle, String boardContent, String createdAt, String imageUrl,String nickname) {
        this.id = id;
        this.viewCount = viewCount;
        this.categoryId = categoryId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
        this.nickname = nickname;
    }
}