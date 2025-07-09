package com.project.nyang.modules.board.reveiw.repository;

import com.project.nyang.modules.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 입양 후기 게시판 Repository
 *
 * @author : 박세정
 * @fileName : ReviewBoardRepository
 * @since : 2025-07-08
 */
public interface ReviewBoardRepository extends JpaRepository<Board, Long> {
}