package com.project.nyang.global.searchlog.domain;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 *
 * 엘라스틱 서치에 저장되는 검색 데이터
 * @fileName        : SearchLogDocument
 * @author          : 박세정
 * @since           : 2025-07-16
 *
 */
@Document(indexName = "search-log-index")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchLogDocument {
    @Id
    private String id;
    private String keyword;
    private String searchedAt;
}
