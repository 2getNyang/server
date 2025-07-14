package com.project.nyang.modules.image.repository;

import com.project.nyang.modules.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 이미지 레포지토리입니다.
 *
 * @author : 선순주
 * @fileName : repository
 * @since : 2025-07-13
 */
public interface ImageRepository extends JpaRepository<Image, Long> {
    //소프트 딜리트 된 이미지 중, 삭제된 지(소프트 딜리트 된 지)threshold보다 이전인 이미지들을 가져옴
    //ex : threshold가 3이면, 소프트 딜리트된 이미지 중 3일이 지난 이미지 List를 가져오는것
    List<Image> findByDeletedAtIsNotNullAndDeletedAtBefore(LocalDateTime threshold);
}