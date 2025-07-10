package com.project.nyang.modules.board.reveiw.repository;

import com.project.nyang.modules.board.Board;
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
    Page<Board> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Board> findByIdAndDeletedAtIsNull(Long id);
}