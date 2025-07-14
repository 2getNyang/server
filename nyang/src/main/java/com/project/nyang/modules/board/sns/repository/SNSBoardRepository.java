package com.project.nyang.modules.board.sns.repository;

import com.project.nyang.modules.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * sns board repository
 *
 * @author : 이은서
 * @fileName : BoardRepository
 * @since : 25. 7. 9.
 */
public interface SNSBoardRepository extends JpaRepository<Board,Long> {
    /* categoryId 가 3 이고 deletedAt 이 null 인친구들
    * 상세조회 */
    Page<Board> findByCategory_CategoryIdAndDeletedAtIsNull(Long categoryId, Pageable pageable);
    // 전체조회
    List<Board> findByCategory_CategoryIdAndDeletedAtIsNull(Long categoryId);

    @Query("""
    SELECT b FROM Board b
    WHERE b.category.categoryId = :categoryId
      AND b.deletedAt IS NULL
      AND (
            b.boardTitle LIKE %:keyword%
         OR b.boardContent LIKE %:keyword%
      )
""")/* 검색 */
    Page<Board> searchKeywordSNS(
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword,
            Pageable pageable
    );


}
