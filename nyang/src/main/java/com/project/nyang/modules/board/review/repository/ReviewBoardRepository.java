package com.project.nyang.modules.board.review.repository;

import com.project.nyang.modules.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 입양 후기 게시판 Repository -> 추후 삭제 필요
 *
 * @author : 박세정
 * @fileName : ReviewBoardRepository
 * @since : 2025-07-08
 */
public interface ReviewBoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByIdAndDeletedAtIsNull(Long id);

    Page<Board> findAllByDeletedAtIsNullAndCategory_CategoryId(Pageable pageable, Long categoryCategoryId);
}