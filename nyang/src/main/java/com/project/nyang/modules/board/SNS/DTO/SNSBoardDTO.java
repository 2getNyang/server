package com.project.nyang.modules.board.SNS.DTO;

import com.project.nyang.reference.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * sns board 가 사용하는 dto 입니다.
 *
 * @author : 이은서
 * @fileName : SNSBoardDTO
 * @since : 25. 7. 8.
 */
@Getter
@Setter
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

    public SNSBoardDTO(Long id, Category category, String boardTitle, String boardContent, Long viewCount, String instagramLink, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.category = category;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.viewCount = viewCount;
        this.instagramLink = instagramLink;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }
}
