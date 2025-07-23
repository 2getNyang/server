package com.project.nyang.modules.comment.dto;

import com.project.nyang.modules.comment.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 동물 상세 조회에서 댓글을 조회하기 위한 DTO입니다.
 *
 * @author : 이지은
 * @fileName : AnimalCommentDTO
 * @since : 25. 7. 14.
 */
@Getter
@Builder
@Schema(description = "동물 상세 조회 시 반환되는 댓글 정보")
public class AnimalCommentDTO {
    //댓글 ID
    @Schema(description = "댓글 ID", example = "1")
    private Long commentId;

    //댓글작성한 UserID
    @Schema(description = "작성자 사용자 ID", example = "10")
    private Long userId;

    //댓글작성한 User 닉네임
    @Schema(description = "작성자 닉네임", example = "냥냥이")
    private String nickname;

    //댓글 parent_id
    @Schema(description = "부모 댓글 ID", example = "null")
    private Long parentId;

    //작성한 댓글 내용
    @Schema(description = "댓글 내용", example = "이 아이 너무 귀여워요!")
    private String commentContent;

    //댓글 작성 시간
    @Schema(description = "댓글 작성 시간", example = "2025-07-14T15:30:00")
    private LocalDateTime createdAt;

    // 대댓글(1depth) 리스트만 포함
    @Schema(description = "대댓글 리스트 (1depth만 포함)")
    private List<AnimalCommentDTO> childComments;

    public static AnimalCommentDTO fromEntity(Comment comment) {
        return AnimalCommentDTO.builder()
                .commentId(comment.getId())
                .userId(comment.getUser() != null ? comment.getUser().getId() : null)
                .nickname(comment.getUser() != null ? comment.getUser().getNickname() : null)
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .commentContent(comment.getCommentContent())
                .createdAt(comment.getCreatedAt())
                .childComments(
                        comment.getComments() == null ? List.of() :
                                comment.getComments().stream()
                                        .map(AnimalCommentDTO::fromEntity)
                                        .toList()
                )
                .build();
    }

}