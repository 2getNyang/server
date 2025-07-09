package com.project.nyang.modules.board;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.nyang.global.common.entity.BaseTime;
import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import com.project.nyang.modules.comment.entity.Comment;
import com.project.nyang.modules.image.entity.Image;
import com.project.nyang.modules.like.entity.LikeIt;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.reference.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * board 엔티티 클래스 입니다.
 *
 * @author : 박세정
 * @fileName : Board
 * @since : 2025-07-07
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private PetApplicationForm petApplicationForm;

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
    private LocalDate missingDate;

    @Column(name = "missing_location")
    private String missingLocation;

    @Column
    private String phone;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeIt> likeList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kind_cd", nullable = false)
    private Kind kind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_kind_cd", nullable = false)
    private UpKind upKind;

    //board<->image 연관관계 편의 메서드
    public void addImage(Image image) {
        images.add(image);
        image.builder().board(this).build();

    }

    public void clearImages() {
        for (Image image : images) {
            image.builder().board(null).build();
        }
        images.clear();
    }

    public void increaseViewCount() {
        this.viewCount += 1;
    }

    @Builder
    public Board(LocalDate missingDate, User user, Category category, Region region, SubRegion subRegion, String boardTitle, String boardContent, Long viewCount, String instagramLink, String gender, Integer age, String furColor, String distinctFeatures, String missingLocation, String phone) {
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
