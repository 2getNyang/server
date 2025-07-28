package com.project.nyang.modules.board.repository;

import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.mypage.dto.MyBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

/**
 * 공용 BoardRepository
 *
 * @author : 박세정
 * @fileName : BoardRepository
 * @since : 2025-07-10
 */
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 기본 fetch: images + user
    @EntityGraph(attributePaths = {"images", "user"})
    Page<Board> findWithUserAndImagesByUser_IdAndDeletedAtIsNullAndCategory_CategoryId(
            Long userId,
            Long categoryCategoryId,
            Pageable pageable
    );

    // 확장 fetch: images + user + kind
    @EntityGraph(attributePaths = {"images", "user", "kind"})
    Page<Board> findWithUserImagesAndKindByUser_IdAndDeletedAtIsNullAndCategory_CategoryId(
            Long userId,
            Long categoryCategoryId,
            Pageable pageable
    );
}
