package com.project.nyang.modules.board.entity;

import com.project.nyang.global.common.entity.BaseTime;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.reference.entity.Category;
import com.project.nyang.reference.entity.Region;
import com.project.nyang.reference.entity.SubRegion;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 *
 * board 엔티티 클래스 입니다.
 * @fileName        : Board
 * @author          : 박세정
 * @since           : 2025-07-07
 *
 */
// TODO. BaseTime extends 필요
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_code", nullable = false)
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_region_code", nullable = false)
    private SubRegion subRegion;

    @Column(name = "board_title", nullable = false)
    private String boardTitle;

    @Column(name = "board_content", nullable = false)
    private String boardContent;

    @Column(name = "board_view_count")
    private Long viewCount = 0L;

    @Column(name = "instagram_link")
    private String instagramLink;

    @Column
    private String gender;

    @Column
    private Integer age;

    @Column(name = "fur_color")
    private String furColor;

    @Column(name = "distinct_features")
    private String distinctFeatures;

    @Column(name = "missing_date")
    private Date missingDate;

    @Column(name = "missing_location")
    private String missingLocation;

    @Column
    private String phone;

    // created_at, modified_at, deleted_at 생략

   @Builder
   public Board(Date missingDate, User user, Category category, Region region, SubRegion subRegion, String boardTitle, String boardContent, Long viewCount, String instagramLink, String gender, Integer age, String furColor, String distinctFeatures, String missingLocation, String phone) {
       this.missingDate = missingDate;
       this.user = user;
       this.category = category;
       this.region = region;
       this.subRegion = subRegion;
       this.boardTitle = boardTitle;
       this.boardContent = boardContent;
       this.viewCount = viewCount;
       this.instagramLink = instagramLink;
       this.gender = gender;
       this.age = age;
       this.furColor = furColor;
       this.distinctFeatures = distinctFeatures;
       this.missingLocation = missingLocation;
       this.phone = phone;
   }
}
