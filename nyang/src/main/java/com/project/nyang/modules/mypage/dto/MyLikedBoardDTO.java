package com.project.nyang.modules.mypage.dto;

import com.project.nyang.modules.board.entity.Board;
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
    private Long id;
    private String boardTitle;
    private String nickname;
    private LocalDateTime createdAt;
    private String categoryName;

    @Builder
    public MyLikedBoardDTO(Long id, String boardTitle, String nickname, LocalDateTime createdAt, String categoryName) {
        this.id = id;
        this.boardTitle = boardTitle;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.categoryName = categoryName;
    }

    public static MyLikedBoardDTO of(Board board) {
        return MyLikedBoardDTO.builder()
                .id(board.getId())
                .boardTitle(board.getCategory().getCategoryId() == 4L ? board.getKind().getKindNm() : board.getBoardTitle())
                .nickname(board.getUser().getNickname())
                .createdAt(board.getCreatedAt())
                .categoryName(board.getCategory().getCategoryType())
                .build();
    }
}