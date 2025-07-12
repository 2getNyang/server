package com.project.nyang.modules.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 댓글 수정 요청 시 사용 DTO
 *
 * @author : 박세정
 * @fileName : UpdateCommentDTO
 * @since : 2025-07-11
 */
@Getter
public class UpdateCommentDTO {
    @Schema(description = "댓글 내용")
    private String commentContent;

    @JsonCreator
    @Builder
    public UpdateCommentDTO(String commentContent) {
        this.commentContent = commentContent;
    }
}