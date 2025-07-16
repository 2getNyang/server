package com.project.nyang.modules.comment.dto;

import com.project.nyang.modules.comment.entity.Comment;
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
public class AnimalCommentDTO {
    //댓글 ID
    private Long commentId;
    //댓글작성한 UserID
    private Long userId;
    //댓글작성한 User 닉네임
    private String nickname;
    //작성한 댓글 내용
    private String commentContent;
    //댓글 작성 시간
    private LocalDateTime createdAt;

    // 대댓글(1depth) 리스트만 포함
    private List<AnimalCommentDTO> childComments;

    public static AnimalCommentDTO fromEntity(Comment comment) {
        return AnimalCommentDTO.builder()
                .commentId(comment.getId())
                .userId(comment.getUser() != null ? comment.getUser().getId() : null)
                .nickname(comment.getUser() != null ? comment.getUser().getNickname() : null)
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