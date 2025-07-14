package com.project.nyang.modules.mypage.repository;

import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.like.entity.LikeIt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 임시 Like repository -> 삭제 예정
 *
 * @author : 박세정
 * @fileName : TempLikeRepository
 * @since : 2025-07-12
 */
public interface TempLikeRepository extends JpaRepository<LikeIt, Long> {
    Page<LikeIt> findByUser_IdAndAnimalNotNull(Long userId, Pageable pageable);
}