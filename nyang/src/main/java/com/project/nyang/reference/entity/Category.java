package com.project.nyang.reference.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 카테고리 엔티티
 *
 * @author : 이은서
 * @fileName : Category
 * @since : 25. 7. 7.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATEGORY")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_type",nullable = false)
    private String categoryType;

    @Builder
    public Category(String categoryType){
        this.categoryType = categoryType;
    }

}
