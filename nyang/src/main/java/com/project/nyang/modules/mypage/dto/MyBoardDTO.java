package com.project.nyang.modules.mypage.dto;

import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.image.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자가 작성한 입양후기, sns 홍보 게시글 DTO
 *
 * @author : 박세정
 * @fileName : MyBoardDTO
 * @since : 2025-07-16
 */
@Getter
public class MyBoardDTO {
    @Schema(description = "게시글 ID")
    private Long id;
    @Schema(description = "사용자 닉네임")
    private String nickname;
    @Schema(description = "게시글 제목")
    private String boardTitle;
    @Schema(description = "게시글 내용")
    private String boardContent;
    @Schema(description = "첫번째 사진 url")
    private String imageUrl;
    @Schema(description = "생성일자")
    private LocalDateTime createdAt;
    @Schema(description = "조회수")
    private Long viewCount;

    @Builder
    public MyBoardDTO(Long id, String nickname, String boardTitle, String boardContent, String imageUrl, LocalDateTime createdAt, Long viewCount) {
        this.id = id;
        this.nickname = nickname;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
    }

    public static MyBoardDTO of(Board board) {
        return MyBoardDTO.builder()
                .id(board.getId())
                .nickname(board.getUser().getNickname())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .imageUrl(board.getImages() != null && !board.getImages().isEmpty()
                        ? board.getImages().stream()
                        .filter(image -> image.getDeletedAt() == null && image.getThumbnailIs().equals("Y"))
                        .map(Image::getS3Url)
                        .findFirst()
                        .orElse(null)
                        : null)
                .createdAt(board.getCreatedAt())
                .viewCount(board.getViewCount())
                .build();
    }
}