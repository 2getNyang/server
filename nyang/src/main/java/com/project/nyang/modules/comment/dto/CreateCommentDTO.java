package com.project.nyang.modules.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 댓글 추가/수정 request DTO
 *
 * @author : 박세정
 * @fileName : CreateCommentDTO
 * @since : 2025-07-11
 */
@Getter
public class CreateCommentDTO {
    @Schema(description = "댓글 내용")
    private String commentContent;
    @Schema(description = "상위 댓글 아이디")
    private Long parentId;

    @Builder
    public CreateCommentDTO(String commentContent, Long parentId) {
        this.commentContent = commentContent;
        this.parentId = parentId;
    }
}