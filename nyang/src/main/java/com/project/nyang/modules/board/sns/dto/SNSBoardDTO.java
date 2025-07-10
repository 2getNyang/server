package com.project.nyang.modules.board.sns.dto;

import com.project.nyang.modules.board.Board;
import com.project.nyang.reference.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private Long id;
    private Category category;
    private String boardTitle;
    private String boardContent;
    private Long viewCount;
    private String instagramLink;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    private Long userId;

    public SNSBoardDTO(Board entity) {
        this.id = entity.getId();
        this.category = entity.getCategory();
        this.boardTitle = entity.getBoardTitle();
        this.boardContent = entity.getBoardContent();
        this.viewCount = entity.getViewCount();
        this.instagramLink = entity.getInstagramLink();
        this.createdAt = entity.getCreatedAt();
        this.modifiedAt = entity.getModifiedAt();
        this.deletedAt = entity.getDeletedAt();
    }

    public void updateDto(SNSBoardDTO dto) {
        if (dto.getBoardTitle() != null) {
            this.boardTitle = dto.getBoardTitle();
        }
        if (dto.getBoardContent() != null) {
            this.boardContent = dto.getBoardContent();
        }
        if (dto.getInstagramLink() != null) {
            this.instagramLink = dto.getInstagramLink();
        }
    }

}
