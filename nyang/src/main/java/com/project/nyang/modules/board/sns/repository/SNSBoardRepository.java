package com.project.nyang.modules.board.sns.repository;

import com.project.nyang.modules.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * sns board repository
 *
 * @author : 이은서
 * @fileName : BoardRepository
 * @since : 25. 7. 9.
 */
public interface SNSBoardRepository extends JpaRepository<Board,Long> {
    Page<Board> findByCategory_CategoryId(Long categoryId, Pageable pageable);


}
