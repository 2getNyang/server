package com.project.nyang.modules.board.repository;

import com.project.nyang.modules.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 공용 BoardRepository
 *
 * @author : 박세정
 * @fileName : BoardRepository
 * @since : 2025-07-10
 */
public interface BoardRepository extends JpaRepository<Board, Long> {
}
