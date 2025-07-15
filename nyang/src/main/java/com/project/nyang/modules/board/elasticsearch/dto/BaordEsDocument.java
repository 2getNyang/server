package com.project.nyang.modules.board.elasticsearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import lombok.Builder;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Elasticsearch에 저장되는 게시글 정보 모델
 *
 * @author : 박세정
 * @fileName : BaordEsDocument
 * @since : 2025-07-15
 */
@JsonIgnoreProperties(ignoreUnknown = true) // 해당 설정을 넣지 않으면 class 속성이 들어가게 됨
@Document(indexName = "board-index")
public class BaordEsDocument {
    // 공통
    @Id
    private String id;
    private Long boardViewCount;
    private Long categoryId;

    // 입양 후기, sns 홍보
    private String boardTitle;
    private String boardContent;
    private LocalDateTime createdAt;

    // 실종/목격
    private String lostType;    //MS : 실종 or WT : 목격
    private String kindName;    //품종 -> 실종/목격 게시판의 경우 해당 필드가 title 역할
    private String gender;  // M:수컷 / F : 암컷 / Q : 모름
    private Integer age;
    private String furColor;
    private String missingLocation;
    private LocalDate missingDate;

    @Builder
    public BaordEsDocument(String id, Long boardViewCount, Long categoryId, String boardTitle, String boardContent, LocalDateTime createdAt, String lostType, String kindName, String gender, Integer age, String furColor, String missingLocation, LocalDate missingDate) {
        this.id = id;
        this.boardViewCount = boardViewCount;
        this.categoryId = categoryId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.createdAt = createdAt;
        this.lostType = lostType;
        this.kindName = kindName;
        this.gender = gender;
        this.age = age;
        this.furColor = furColor;
        this.missingLocation = missingLocation;
        this.missingDate = missingDate;
    }
}