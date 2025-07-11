package com.project.nyang.modules.board.lost.dto;

import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.image.entity.Image;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.reference.entity.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 실종/목격 제보 게시판 글 작성 DTO입니다.
 *
 * @author : 선순주
 * @fileName : LostCreateRequestDto
 * @since : 2025-07-09
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LostCreateRequestDto {
    private Long categoryId;
    private Long boardId;
    private Long userId;

    private String lostType;
    private LocalDate missingDate;
    private String missingLocation;
    private String regionName;
    private String subRegionName;
    private String phone;
    private String upKindName;      //축종
    private String kindName;        //품종
    private String gender;
    private Integer age;
    private String furColor;
    private String content;
    private String distinctFeatures;
    private List<String> imageUrls;

    public Board toEntity(User user, Category category, List<Image> imageUrls,
                          Region region, SubRegion subRegion, UpKind upKind, Kind kind) {
        return Board.builder()
                .category(category)
                .user(user)
                .region(region)
                .subRegion(subRegion)
                .upKind(upKind)
                .kind(kind)
                .lostType(this.lostType)
                .missingDate(this.missingDate)
                .missingLocation(this.missingLocation)
                .distinctFeatures(this.distinctFeatures)
                .phone(this.phone)
                .gender(this.gender)
                .age(this.age)
                .furColor(this.furColor)
                .boardContent(this.content)
                .images(imageUrls == null ? new ArrayList<>() : imageUrls)
                .build();
    }

}