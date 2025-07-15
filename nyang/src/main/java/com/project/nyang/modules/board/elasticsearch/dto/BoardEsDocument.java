package com.project.nyang.modules.board.elasticsearch.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Getter
@NoArgsConstructor
public class BoardEsDocument {
    // 공통
    @Id
    private String id;
    private Long viewCount;
    private Long categoryId;
    private String imageUrl;

    // 입양 후기, sns 홍보
    private String boardTitle;
    private String boardContent;
    private String createdAt;

    // 실종/목격
    private String lostType;    //MS : 실종 or WT : 목격
    private String kindName;    //품종 -> 실종/목격 게시판의 경우 해당 필드가 title 역할
    private String gender;  // M:수컷 / F : 암컷 / Q : 모름
    private Integer age;
    private String furColor;
    private String missingLocation;
    private String missingDate;

    @Builder
    public BoardEsDocument(String id, Long viewCount, Long categoryId, String boardTitle, String boardContent, String createdAt, String lostType, String kindName, String gender, Integer age, String furColor, String missingLocation, String missingDate, String imageUrl) {
        this.id = id;
        this.viewCount = viewCount;
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
        this.imageUrl = imageUrl;
    }

    public static BoardListDTO toBoardDTO(BoardEsDocument document) {
        return BoardListDTO.builder()
                .id(document.getId())
                .viewCount(document.getViewCount())
                .categoryId(document.getCategoryId())
                .boardTitle(document.getBoardTitle())
                .boardContent(document.getBoardContent())
                .createdAt(LocalDateTime.parse(document.getCreatedAt()))
                .imageUrl(document.getImageUrl())
                .build();
    }

    public static LostBoardListDTO toLostBoardDTO(BoardEsDocument document) {
        return LostBoardListDTO.builder()
                .id(document.getId())
                .viewCount(document.getViewCount())
                .categoryId(document.getCategoryId())
                .lostType(document.getLostType())
                .kindName(document.getKindName())
                .gender(document.getGender())
                .age(document.getAge())
                .furColor(document.getFurColor())
                .missingLocation(document.getMissingLocation())
                .missingDate(LocalDate.parse(document.getMissingDate()))
                .imageUrl(document.getImageUrl())
                .build();
    }

    public void increaseViewCount() {
        this.viewCount += 1;
    }

}