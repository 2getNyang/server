package com.project.nyang.modules.board.sns.repository;

import com.project.nyang.modules.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * sns board repository
 *
 * @author : 이은서
 * @fileName : BoardRepository
 * @since : 25. 7. 9.
 */
public interface SNSBoardRepository extends JpaRepository<Board,Long> {
}
