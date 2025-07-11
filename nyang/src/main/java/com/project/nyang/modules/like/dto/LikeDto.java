package com.project.nyang.modules.like.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 좋아요/좋아요 취소 reponse DTO
 *
 * @author : 박세정
 * @fileName : LikeDto
 * @since : 2025-07-10
 */
@Getter
public class LikeDto {
    @Schema(description = "좋아요 수")
    private Long likeCount;
    @Schema(description = "좋아요 여부")
    private boolean isLiked;

    @Builder
    public LikeDto(Long likeCount, boolean isLiked) {
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }
}