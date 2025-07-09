package com.project.nyang.modules.board.sns.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * sns board dto 
 * @fileName        : SNSBoardDTO
 * @author          : 이은서
 * @since           : 25. 7. 9.
 * 
 */
@Getter
@Setter
public class SNSBoardDTO {
    private Long id;
    private String title;
    private String content;
    private String instagramLink;
    private Long viewCount;
}
