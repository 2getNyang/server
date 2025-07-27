package com.project.nyang.modules.mypage.service;

import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import com.project.nyang.modules.adoption.repository.AdoptionRepository;
import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.board.repository.BoardRepository;
import com.project.nyang.modules.like.entity.LikeIt;
import com.project.nyang.modules.like.repository.LikeRepository;
import com.project.nyang.modules.mypage.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AdoptionRepository adoptionRepository;

    @Transactional(readOnly = true)
    public Page<MyAnimalDTO> getMyAnimals(Long userId, int page, int size) {
        Page<LikeIt> likeIts = likeRepository.findByUser_IdAndAnimalIsNotNull(userId, PageRequest.of(page, size));

        return likeIts.map(likeIt -> {
            return MyAnimalDTO.of(likeIt.getAnimal()); // Animal → DTO 변환
        });
    }

    @Transactional(readOnly = true)
    public Page<MyLikedBoardDTO> getLikedBoards(Long userId, int page, int size) {
        Page<LikeIt> likeIts = likeRepository.findByUser_IdAndBoardIsNotNull(userId, PageRequest.of(page, size));

        return likeIts.map(likeIt -> {
            return MyLikedBoardDTO.of(likeIt.getBoard());
        });
    }

    @Transactional(readOnly = true)
    public Page<MyBoardDTO> getMyBoards(Long userId, Long categoryId, int page, int size) {
        Page<Board> boards = boardRepository.findWithUserAndImagesByUser_IdAndDeletedAtIsNullAndCategory_CategoryId(userId, categoryId, PageRequest.of(page, size));

        return boards.map(MyBoardDTO::of);
    }

    @Transactional(readOnly = true)
    public Page<MyLostBoardDTO> getMyLostBoards(Long userId, Long categoryId, int page, int size) {
        Page<Board> boards = boardRepository.findWithUserImagesAndKindByUser_IdAndDeletedAtIsNullAndCategory_CategoryId(userId, categoryId, PageRequest.of(page, size));

        return boards.map(MyLostBoardDTO::of);
    }

    @Transactional(readOnly = true)
    public Page<MyPetApplicationFormDTO> getPetApplicationForms(Long userId, int page, int size) {
        Page<PetApplicationForm> forms = adoptionRepository.findByUser_Id(userId, PageRequest.of(page, size));

        return forms.map(MyPetApplicationFormDTO::of);
    }

    @Transactional(readOnly = true)
    public MyPetApplicationDetailDTO getPetApplicationDetail(Long userId, Long formId) {
        PetApplicationForm form = adoptionRepository.findById(formId).orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));

        if(!form.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        return MyPetApplicationDetailDTO.toDTO(form);
    }
}