package com.project.nyang.modules.mypage.service;

import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.board.repository.BoardRepository;
import com.project.nyang.modules.like.entity.LikeIt;
import com.project.nyang.modules.like.repository.LikeRepository;
import com.project.nyang.modules.mypage.dto.MyAnimalDTO;
import com.project.nyang.modules.mypage.dto.MyLikedBoardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;

    public Page<MyAnimalDTO> getMyAnimals(Long userId, int page, int size) {
        Page<LikeIt> likeIts = likeRepository.findByUser_IdAndAnimalNotNull(userId, PageRequest.of(page, size));

        return likeIts.map(likeIt -> {
            return MyAnimalDTO.of(likeIt.getAnimal()); // Animal → DTO 변환
        });
    }

    public Page<MyLikedBoardDTO> getLikedBoards(Long userId, int page, int size) {
        Page<LikeIt> likeIts = likeRepository.findByUser_idAndBoardNotNull(userId, PageRequest.of(page, size));

        return likeIts.map(likeIt -> {
            return MyLikedBoardDTO.of(likeIt.getBoard());
        });
    }
}