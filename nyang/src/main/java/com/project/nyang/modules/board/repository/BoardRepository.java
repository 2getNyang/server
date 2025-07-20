package com.project.nyang.modules.board.repository;

import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.mypage.dto.MyBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

/**
 * 공용 BoardRepository
 *
 * @author : 박세정
 * @fileName : BoardRepository
 * @since : 2025-07-10
 */
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByUser_IdAndDeletedAtIsNullAndCategory_CategoryId(Long userId, Long categoryCategoryId, Pageable pageable);
}
