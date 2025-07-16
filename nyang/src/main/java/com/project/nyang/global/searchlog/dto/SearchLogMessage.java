package com.project.nyang.global.searchlog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * kafka로 주고받는 메세지 포멧(DTO)
 * @fileName        : SearchLogMessage
 * @author          : 박세정
 * @since           : 2025-07-16
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchLogMessage {
    private String keyword;     //검색한 키워드
    private String searchedAt;  //검색한 시간
}
