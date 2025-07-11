package com.project.nyang.modules.like.repository;

import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 임시 BoardRepository -> 삭제 필요
 *
 * @author : 박세정
 * @fileName : BoardRepository
 * @since : 2025-07-10
 */
public interface BoardRepository extends JpaRepository<Board, Long> {
}
