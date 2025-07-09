package com.project.nyang.modules.board.SNS.repository;

import com.project.nyang.modules.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Snsboard repository 입니다
 *
 * @author : 이은서
 * @fileName : SNSBoardRepository
 * @since : 25. 7. 8.
 */
@Repository
public interface SNSBoardRepository extends JpaRepository<Board,Long> {

}
