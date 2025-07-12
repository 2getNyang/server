package com.project.nyang.modules.comment.entity;

import com.project.nyang.global.common.entity.BaseTime;
import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.reference.entity.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * comment 엔티티 클래스입니다.
 * @fileName        : Comment
 * @author          : 박세정
 * @since           : 2025-07-07
 *
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desertion_no")
    private Animal animal;

    //여러개의 댓글은 하나의 부모 댓글을 가짐
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent; //대댓글

    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // created_at, modified_at, deleted_at 생략

   @Builder
   public Comment(User user, Board board, Animal animal, Comment parent, String commentContent) {
       this.user = user;
       this.board = board;
       this.animal = animal;
       this.parent = parent;
       this.commentContent = commentContent;
   }

   public void updateComment(String content) {
       this.commentContent = content;
   }

}
