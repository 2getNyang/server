package com.project.nyang.modules.board.lost.repository;

import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.board.lost.dto.LostListResponseDTO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 실종/목격 게시판 레포지토리입니다
 *
 * @author : 선순주
 * @fileName : LostRepository
 * @since : 2025-07-09
 */

@Repository
public interface LostRepository extends JpaRepository<Board, Long> {

    // 카테고리 ID 기반 페이징된 게시글 조회(deletedAt이 null인것만 조회)
    Page<Board> findByCategory_CategoryIdAndDeletedAtIsNull(Long categoryId, Pageable pageable);

    //N+1 방지 EntityGraph 사용. 게시글 단일 조회
    @EntityGraph(attributePaths = {"user", "category", "kind", "region", "subRegion", "images"})
    Optional<Board> findWithDetailsById(Long id);

    //N+1 방지 EntityGraph 사용. delete되지 않은 게시글 조회
    @EntityGraph(attributePaths = {"images"})
    Optional<Board> findByIdAndDeletedAtIsNull(Long boardId);

    //삭제되지 않은 게시글 + 썸네일 이미지 가져오는 쿼리
    @Query("""
    SELECT new com.project.nyang.modules.board.lost.dto.LostListResponseDTO(
        b.id,
        b.category.categoryId,
        b.user.id,
        b.user.nickname,
        b.lostType,
        b.viewCount,
        b.kind.kindNm,
        b.age,
        b.furColor,
        b.gender,
        b.missingLocation,
        b.missingDate,
        (SELECT i.s3Url FROM Image i WHERE i.board.id = b.id AND i.thumbnailIs = 'Y' AND i.deletedAt IS NULL),
        b.createdAt,
        b.deletedAt
    )
    FROM Board b
    WHERE b.category.categoryId = :categoryId
      AND b.deletedAt IS NULL
""")
    Page<LostListResponseDTO> findAllLostBoardsWithThumbnail(@Param("categoryId") Long categoryId, Pageable pageable);


}
