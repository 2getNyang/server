package com.project.nyang.modules.comment.repository;

import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 댓글 Repository
 *
 * @author : 박세정
 * @fileName : CommentRepository
 * @since : 2025-07-11
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByAnimal_DesertionNo(String desertionNo);
}
