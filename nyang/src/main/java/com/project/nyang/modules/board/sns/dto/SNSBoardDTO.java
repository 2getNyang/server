package com.project.nyang.modules.board.sns.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.nyang.modules.board.entity.Board;

import com.project.nyang.modules.comment.entity.Comment;
import com.project.nyang.modules.image.entity.Image;
import com.project.nyang.modules.user.entity.User;
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

    @Schema(description = "게시글 ID", example = "1")
    private Long id;

    @Schema(description = "카테고리 ID", example = "3")
    private Long category;

    @Schema(description = "게시글 제목", example = "고양이 귀여워요")
    private String boardTitle;

    @Schema(description = "게시글 내용", example = "오늘 본 고양이는 너무 귀여웠어요~")
    private String boardContent;

    @Schema(description = "조회수", example = "10")
    private Long viewCount;

    @Schema(description = "인스타그램 링크", example = "https://www.instagram.com/reel/abc123/")
    private String instagramLink;

    @Schema(description = "이미지 S3 URL 리스트", example = "[\"https://s3.amazonaws.com/bucket/image1.png\"]")
    @JsonIgnoreProperties({"board"})
    private List<String> images;

    @Schema(description = "게시글 생성일시", example = "2025-07-15T12:34:56")
    private LocalDateTime createdAt;

    @Schema(description = "게시글 수정일시", example = "2025-07-15T13:00:00")
    private LocalDateTime modifiedAt;

    @Schema(description = "게시글 삭제일시 (삭제된 경우만 존재)", example = "2025-07-16T10:00:00")
    private LocalDateTime deletedAt;

    @Schema(description = "좋아요 수", example = "5")
    private Long likeCount;

    @Schema(description = "댓글 리스트")
    private List<CommentDTO> comments;

    @Schema(description = "작성자 ID", example = "1")
    private Long userId;

    @Schema(description = "작성자 닉네임", example = "냥냥이")
    private String nickname;

    public Board toEntity(User user, Category category, List<Image> images) {
        return Board.builder()
                .user(user)
                .category(category)
                .images(images)
                .id(this.id)
                .boardTitle(this.boardTitle)
                .boardContent(this.boardContent)
                .viewCount(this.viewCount)
                .instagramLink(this.instagramLink)
                .build();
    }

    @Getter
    public static class CommentDTO {

        @Schema(description = "댓글 ID", example = "101")
        private Long id;

        @Schema(description = "댓글 내용", example = "정말 귀엽네요!")
        private String commentContent;

        @Schema(description = "댓글 작성 시간", example = "2025-07-15T12:45:00")
        private LocalDateTime createdAt;

        @Schema(description = "댓글 작성자 닉네임", example = "고양집사")
        private String commentNickname;

        @Schema(description = "부모 댓글 ID (대댓글일 경우)", example = "100")
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
