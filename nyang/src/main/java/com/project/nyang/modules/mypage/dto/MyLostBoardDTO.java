package com.project.nyang.modules.mypage.dto;

import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.image.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 사용자가 작성한 실종/목격 홍보 게시글 DTO
 *
 * @author : 박세정
 * @fileName : MyLostBoardDTO
 * @since : 2025-07-16
 */
@Getter
public class MyLostBoardDTO {
    @Schema(description = "게시글 ID")
    private Long id;
    @Schema(description = "조회수")
    private Long viewCount;
    @Schema(description = "게시글 타입. (MS:실종 / WT:목격)", example = "MS")
    private String lostType;    //MS : 실종 or WT : 목격
    @Schema(description = "품종", example = "레그돌")
    private String kindName;    //품종 -> 실종/목격 게시판의 경우 해당 필드가 title 역할
    @Schema(description = "성별(M:수컷 / F:암컷 / Q:모름)", example = "F")
    private String gender;  // M:수컷 / F : 암컷 / Q : 모름
    @Schema(description = "나이", example = "3")
    private Integer age;
    @Schema(description = "털 색깔", example = "갈색")
    private String furColor;
    @Schema(description = "실종/목격 장소", example = "부천역 3번 출구 근처")
    private String missingLocation;
    @Schema(description = "실종/목격 날짜", example = "2025-07-12")
    private LocalDate missingDate;
    @Schema(description = "대표 이미지 url")
    private String imageUrl;
    @Schema(description = "작성자 닉네임")
    private String nickname;

    @Builder
    public MyLostBoardDTO(Long id, String nickname, Long viewCount, String lostType, String kindName, String gender, Integer age, String furColor, String missingLocation, LocalDate missingDate, String imageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.viewCount = viewCount;
        this.lostType = lostType;
        this.kindName = kindName;
        this.gender = gender;
        this.age = age;
        this.furColor = furColor;
        this.missingLocation = missingLocation;
        this.missingDate = missingDate;
        this.imageUrl = imageUrl;
    }

    public static MyLostBoardDTO of(Board board) {
        return MyLostBoardDTO.builder()
                .id(board.getId())
                .nickname(board.getUser().getNickname())
                .viewCount(board.getViewCount())
                .lostType(board.getLostType())
                .lostType(board.getLostType())
                .kindName(board.getKind().getKindNm())
                .gender(board.getGender())
                .age(board.getAge())
                .furColor(board.getFurColor())
                .missingLocation(board.getMissingLocation())
                .missingDate(board.getMissingDate())
                .imageUrl(board.getImages() != null && !board.getImages().isEmpty()
                        ? board.getImages().stream()
                        .filter(image -> image.getDeletedAt() == null && image.getThumbnailIs().equals("Y"))
                        .map(Image::getS3Url)
                        .findFirst()
                        .orElse(null)
                        : null)
                .build();
    }
}