package com.project.nyang.modules.board.sns.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SNS Board DTO
 * @fileName : SNSBoardDTO
 * @author : 이은서
 * @since : 25. 7. 9.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SNSBoardDTO {
    private Long id;
    private String title;
    private String content;
    private String instagramLink;
    private Long viewCount;
}
