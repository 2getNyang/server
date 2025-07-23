package com.project.nyang.modules.comment.repository;

import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 댓글 Repository
 *
 * @author : 박세정, 이지은
 * @fileName : CommentRepository
 * @since : 2025-07-11
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("""
        SELECT DISTINCT c FROM Comment c
        LEFT JOIN FETCH c.user
        LEFT JOIN FETCH c.parent
        LEFT JOIN FETCH c.comments
        WHERE c.animal.desertionNo = :desertionNo
    """)
    List<Comment> findWithUserAndParentAndChildrenByAnimalDesertionNo(@Param("desertionNo") String desertionNo);
}
