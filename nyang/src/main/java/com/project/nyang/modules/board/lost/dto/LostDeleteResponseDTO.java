package com.project.nyang.modules.board.lost.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 실종/목격 게시판 글 삭제 DTO입니다.(Soft Delete)
 *
 * @author : 선순주
 * @fileName : LostDeleteResponse
 * @since : 2025-07-12
 */
@Getter
@AllArgsConstructor
public class LostDeleteResponseDTO {
    @Schema(description = "게시글 ID", example = "12")
    private Long boardId;
    @Schema(description = "삭제 일자", example = "2025-07-13 14:30:00")
    private LocalDateTime deletedAt;
}