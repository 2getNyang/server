package com.project.nyang.modules.board.sns.repository;

import com.project.nyang.modules.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * sns board repository
 *
 * @author : 이은서
 * @fileName : BoardRepository
 * @since : 25. 7. 9.
 */
public interface SNSBoardRepository extends JpaRepository<Board,Long> {
    Page<Board> findByCategory_CategoryIdAndDeletedAtIsNull(Long categoryId, Pageable pageable);
    List<Board> findByCategory_CategoryIdAndDeletedAtIsNull(Long categoryId);

    @Query("""
    SELECT b FROM Board b 
    WHERE b.category.categoryId = :categoryId
      AND b.deletedAt IS NULL
      AND (
            b.boardTitle LIKE %:keyword%
         OR b.boardContent LIKE %:keyword%
      )
""")
    Page<Board> searchKeywordSNS(
            Long categoryId1, String titleKeyword, Long categoryId2, String contentKeyword, Pageable pageable
    );

}
