package com.project.nyang.modules.mypage.service;

import com.project.nyang.global.common.api.ApiErrorResponse;
import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.animal.repository.AnimalRepository;
import com.project.nyang.modules.like.entity.LikeIt;
import com.project.nyang.modules.mypage.dto.MyAnimalListDTO;
import com.project.nyang.modules.mypage.repository.TempLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 마이페이지 service
 *
 * @author : 박세정
 * @fileName : MyPageService
 * @since : 2025-07-12
 */
@Service
@RequiredArgsConstructor
public class MyPageService {

    private final TempLikeRepository likeRepository;

    public Page<MyAnimalListDTO> getMyAnimals(Long userId, int page, int size) {
        Page<LikeIt> likeIts = likeRepository.findByUser_IdAndAnimalNotNull(userId, PageRequest.of(page, size));

        return likeIts.map(likeIt -> {
            return MyAnimalListDTO.of(likeIt.getAnimal()); // Animal → DTO 변환
        });
    }
}