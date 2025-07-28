package com.project.nyang.modules.board.review.repository;

import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.image.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 입양 후기 게시판 Repository -> 추후 삭제 필요
 *
 * @author : 박세정
 * @fileName : ReviewBoardRepository
 * @since : 2025-07-08
 */
public interface ReviewBoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b " +
            "LEFT JOIN FETCH b.petApplicationForm pf " +
            "LEFT JOIN FETCH pf.shelter s " +
            "LEFT JOIN FETCH s.region " +
            "LEFT JOIN FETCH s.subRegion " +
            "LEFT JOIN FETCH b.user " +
            "LEFT JOIN FETCH b.comments " +
            "LEFT JOIN FETCH pf.animal " +
            "WHERE b.id = :id AND b.deletedAt IS NULL")
    Optional<Board> findByIdAndDeletedAtIsNull(Long id);

    @EntityGraph(attributePaths = {"user", "images"})
    Page<Board> findAllByDeletedAtIsNullAndCategory_CategoryId(Pageable pageable, Long categoryCategoryId);

}