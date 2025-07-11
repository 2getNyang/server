package com.project.nyang.modules.board.lost.repository;

import com.project.nyang.modules.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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


}
