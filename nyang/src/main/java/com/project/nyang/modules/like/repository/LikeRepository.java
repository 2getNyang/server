package com.project.nyang.modules.like.repository;

import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.like.entity.LikeIt;
import com.project.nyang.modules.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * 좋아요 Repository
 *
 * @author : 박세정
 * @fileName : LikeRepository
 * @since : 2025-07-10
 */
public interface LikeRepository extends JpaRepository<LikeIt, Long> {

    Long countByBoard(Board board);

    Long countByBoardId(Long boardId);

    Long countByAnimal(Animal animal);

    Long countByAnimal_DesertionNo(String animalDesertionNo);

    Optional<LikeIt> findByUser_IdAndAnimal_DesertionNo(Long userId, String animalDesertionNo);

    Optional<LikeIt> findByUser_IdAndBoard_Id(Long userId, Long boardId);

    boolean existsByUserAndAnimal(User user, Animal animal);

    boolean existsByUserIdAndBoardId(Long userId, Long boardId);

    boolean existsByUserIdAndAnimal_DesertionNo(Long userId, String desertionNo);

    // 동물에 대한 좋아요
    @EntityGraph(attributePaths = {"animal"})
    Page<LikeIt> findByUser_IdAndAnimalIsNotNull(Long userId, Pageable pageable);

    // 게시글에 대한 좋아요
    @EntityGraph(attributePaths = {
            "board",
            "board.category",
            "board.kind",
            "board.user",
            "board.images"
    })
    @Query("SELECT l FROM LikeIt l WHERE l.user.id = :userId AND l.board IS NOT NULL")
    Page<LikeIt> findByUser_IdAndBoardIsNotNull(@Param("userId") Long userId, Pageable pageable);

}
