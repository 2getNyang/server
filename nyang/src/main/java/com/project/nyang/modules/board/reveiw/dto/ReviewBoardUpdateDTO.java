package com.project.nyang.modules.board.reveiw.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 게시글을 수정할 때 사용자에게 받는 정보를 담은 DTO
 *
 * @author : 박세정
 * @fileName : ReviewBoardUpdateDTO
 * @since : 2025-07-10
 */
@Getter
public class ReviewBoardUpdateDTO {
    @Schema(description = "게시글 제목", example = "제목입니다")
    private String boardTitle;
    @Schema(description = "게시글 내용", example = "게시글 내용입니다")
    private String boardContent;
    @Schema(description = "입양 신청 아이디", example = "1")
    private Long formId;
    @Schema(
            description = "유지할 이미지 ID 리스트 (기존 이미지 중 삭제하지 않을 이미지의 imageId 목록)",
            example = "[11, 12, 13]"
    )
    private List<Long> remainImageIds; // 유지할 이미지 ID 목록
}