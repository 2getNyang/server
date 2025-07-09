package com.project.nyang.modules.board.reveiw.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 입양 후기 게시판 DTO
 *
 * @author : 박세정
 * @fileName : ReveiwBoardDTO
 * @since : 2025-07-08
 */
@Getter
public class ReviewBoardDTO {
    @Schema(description = "게시물 아이디", example = "1")
    private Long id;
    @Schema(description = "카테고리 아이디", example = "2")
    private Long categoryId;
    @Schema(description = "사용자 아이디", example = "1")
    private Long userId;
    @Schema(description = "게시글 제목", example = "제목입니다")
    private String boardTitle;
    @Schema(description = "게시글 내용", example = "게시글 내용입니다")
    private String boardContent;
    @Schema(description = "조회수", example = "0")
    private Long viewCount;
    @Schema(description = "입양 신청 아이디", example = "1")
    private Long formId;

    @Builder
    public ReviewBoardDTO(Long id, Long categoryId, Long userId, String boardTitle, String boardContent, Long viewCount, Long formId) {
        this.id = id;
        this.categoryId = categoryId;
        this.userId = userId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.viewCount = viewCount;
        this.formId = formId;
    }
}