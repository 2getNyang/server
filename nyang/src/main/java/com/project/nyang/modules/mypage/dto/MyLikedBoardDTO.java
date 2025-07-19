package com.project.nyang.modules.mypage.dto;

import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.image.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자가 좋아요한 게시글 조회 DTO
 *
 * @author : 박세정
 * @fileName : MyLikedBoardDTO
 * @since : 2025-07-14
 */
@Getter
public class MyLikedBoardDTO {
    @Schema(description = "게시글 ID")
    private Long id;
    @Schema(description = "게시글 제목")
    private String boardTitle;
    @Schema(description = "작성자 닉네임")
    private String nickname;
    @Schema(description = "게시일")
    private LocalDateTime createdAt;
    @Schema(description = "게시판 카테고리")
    private String categoryName;
    @Schema(description = "게시판 썸네일")
    private String imageUrl;

    @Builder
    public MyLikedBoardDTO(Long id, String boardTitle, String nickname, LocalDateTime createdAt, String categoryName, String imageUrl) {
        this.id = id;
        this.boardTitle = boardTitle;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.categoryName = categoryName;
        this.imageUrl = imageUrl;
    }

    public static MyLikedBoardDTO of(Board board) {
        return MyLikedBoardDTO.builder()
                .id(board.getId())
                .boardTitle(board.getCategory().getCategoryId() == 4L ? board.getKind().getKindNm() : board.getBoardTitle())
                .nickname(board.getUser().getNickname())
                .createdAt(board.getCreatedAt())
                .categoryName(board.getCategory().getCategoryType())
                .imageUrl(
                        board.getImages() != null && !board.getImages().isEmpty()
                                ? board.getImages().stream()
                                .filter(image -> image.getDeletedAt() == null && image.getThumbnailIs().equals("Y"))
                                .map(Image::getS3Url)
                                .findFirst()
                                .orElse(null)
                                : null
                )
                .build();
    }
}