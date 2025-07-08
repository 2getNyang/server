package com.project.nyang.modules.like.entity;

import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.reference.entity.Category;
import jakarta.persistence.*;
import lombok.*;

/**
 * 좋아요 엔티티
 *
 * @author : 이은서
 * @fileName : LikeIt
 * @since : 25. 7. 7.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LIKE_IT")
public class LikeIt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desertion_no")
    private Animal animal;

    @Builder
    public LikeIt(User user, Category category, Board board, Animal animal) {
        this.user = user;
        this.category = category;
        this.board = board;
        this.animal = animal;
    }

}
