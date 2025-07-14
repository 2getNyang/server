package com.project.nyang.modules.board.sns.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.nyang.modules.board.entity.Board;

import com.project.nyang.modules.comment.entity.Comment;
import com.project.nyang.modules.image.entity.Image;
import com.project.nyang.reference.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/*
 * sns board 가 사용하는 dto 입니다.
 *
 * @author : 이은서
 * @fileName : SNSBoardDTO
 * @since : 25. 7. 8.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SNSBoardDTO {
    @Schema(description = "" ,example = "")
    private Long id;

    @Schema(description = "" ,example = "")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;

    @Schema(description = "" ,example = "")
    private String boardTitle;

    @Schema(description = "" ,example = "")
    private String boardContent;

    @Schema(description = "" ,example = "")
    private Long viewCount;

    @Schema(description = "" ,example = "")
    private String instagramLink;

    @Schema(description = "" ,example = "")
    // 순환참조 막기위해서 JsonIgnoreProperties 추가
    @JsonIgnoreProperties({"board"})
    private List<Image> images;

    @Schema(description = "" ,example = "")
    private LocalDateTime createdAt;

    @Schema(description = "" ,example = "")
    private LocalDateTime modifiedAt;

    @Schema(description = "" ,example = "")
    private LocalDateTime deletedAt;

    @Schema(description = "좋아요 수", example = "5")
    private Long likeCount;

    @Schema(description = "댓글 리스트")
    private List<CommentDTO> comments;

    @Schema(description = "" ,example = "")
    private Long userId;

    @Schema(description = "" ,example = "")
    private String nickname;

    public SNSBoardDTO(Board entity) {
        this.id = entity.getId();
        this.category = entity.getCategory();
        this.boardTitle = entity.getBoardTitle();
        this.boardContent = entity.getBoardContent();
        this.viewCount = entity.getViewCount();
        this.instagramLink = entity.getInstagramLink();
        this.images = entity.getImages();
        this.nickname = entity.getUser().getNickname();
        this.createdAt = entity.getCreatedAt();
        this.modifiedAt = entity.getModifiedAt();
        this.deletedAt = entity.getDeletedAt();

    }

    @Getter
    public static class CommentDTO {
        @Schema(description = "댓글 ID")
        private Long id;
        @Schema(description = "댓글 내용")
        private String commentContent;
        @Schema(description = "댓글 생성 시간")
        private LocalDateTime createdAt;
        @Schema(description = "댓글 작성자")
        private String commentNickname;
        @Schema(description = "부모 댓글 ID")
        private Long parentId;

        @Builder
        public CommentDTO(String commentContent, LocalDateTime createdAt, String commentNickname, Long parentId, Long id) {
            this.id = id;
            this.commentContent = commentContent;
            this.createdAt = createdAt;
            this.commentNickname = commentNickname;
            this.parentId = parentId;
        }

        public static SNSBoardDTO.CommentDTO toDTO(Comment comment) {
            return SNSBoardDTO.CommentDTO.builder()
                    .commentContent(comment.getCommentContent())
                    .createdAt(comment.getCreatedAt())
                    .commentNickname(comment.getUser().getNickname())
                    .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                    .id(comment.getId())
                    .build();
        }
    }

}
