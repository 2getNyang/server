package com.project.nyang.modules.board.reveiw.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 게시글을 생성하거나 수정할 때 사용자에게 받는 정보를 담은 DTO
 *
 * @author : 박세정
 * @fileName : ReviewBoardCreateDTO
 * @since : 2025-07-10
 */
@Getter
public class ReviewBoardCreateDTO {
    @Schema(description = "게시글 제목", example = "제목입니다")
    private String boardTitle;
    @Schema(description = "게시글 내용", example = "게시글 내용입니다")
    private String boardContent;
    @Schema(description = "입양 신청 아이디", example = "1")
    private Long formId;

    @Builder
    public ReviewBoardCreateDTO(String boardTitle, String boardContent, Long formId) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.formId = formId;
    }
}