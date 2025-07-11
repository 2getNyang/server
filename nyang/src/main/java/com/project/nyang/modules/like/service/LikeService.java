package com.project.nyang.modules.like.service;

import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.like.dto.LikeDto;
import com.project.nyang.modules.like.entity.LikeIt;
import com.project.nyang.modules.like.repository.TempAnimalRepository;
import com.project.nyang.modules.like.repository.BoardRepository;
import com.project.nyang.modules.like.repository.LikeRepository;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import com.project.nyang.reference.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 좋아요 Service
 *
 * @author : 박세정
 * @fileName : LikeService
 * @since : 2025-07-10
 */
@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final TempAnimalRepository tempAnimalRepository;

    /**
     * 게시글에 좋아요를 추가하는 메서드
     * @param userId: 사용자 ID
     * @param boardId: 게시글 ID
     * @return
     */
    public LikeDto createLike(Long userId, Long boardId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new IllegalArgumentException("사용자 권한이 없습니다."));

        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new IllegalArgumentException("게시글이 없습니다."));

        boolean alreadyLiked = likeRepository.existsByUserAndBoard(user, board);
        if (alreadyLiked) {
            throw new IllegalStateException("이미 좋아요를 누른 게시글입니다.");
        }

        LikeIt like = LikeIt.builder()
                .user(user)
                .board(board)
                .build();

        likeRepository.save(like);

        return LikeDto.builder()
                .isLiked(true)
                .likeCount(likeRepository.countByBoard(board))
                .build();

    }

    /**
     * 입양공고에 찜을 추가하는 메서드
     * @param userId: 사용자 ID
     * @param desertionNo: 공고 번호
     * @return
     */
    public LikeDto createBookmark(Long userId, String desertionNo) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new IllegalArgumentException("사용자 권한이 없습니다."));

        Animal animal = tempAnimalRepository.findByDesertionNo(desertionNo).orElseThrow(()
                -> new IllegalArgumentException("공고 번호가 잘못되었습니다."));

        boolean alreadyLiked = likeRepository.existsByUserAndAnimal(user, animal);
        if (alreadyLiked) {
            throw new IllegalStateException("이미 좋아요를 누른 게시글입니다.");
        }

        LikeIt like = LikeIt.builder()
                .user(user)
                .animal(animal)
                .build();

        likeRepository.save(like);

        return LikeDto.builder()
                .isLiked(true)
                .likeCount(likeRepository.countByAnimal(animal))
                .build();
    }

    /**
     * 게시글에 좋아요를 삭제하는 메서드
     * @param userId: 사용자 ID
     * @param boardId: 게시글 ID
     * @return
     */
    public LikeDto deleteLike(Long userId, Long boardId) {
        userRepository.findById(userId).orElseThrow(()
                -> new IllegalArgumentException("사용자 권한이 없습니다."));

        LikeIt likeIt = likeRepository.findByUser_IdAndBoard_Id(userId, boardId).orElseThrow(()
                -> new IllegalArgumentException("좋아요 내역이 없습니다."));

        likeRepository.delete(likeIt);

        return LikeDto.builder()
                .isLiked(false)
                .likeCount(likeRepository.countByBoardId(boardId))
                .build();
    }

    /**
     * 입양 공고 찜을 삭제하는 메서드
     * @param userId: 사용자 ID
     * @param desertionNo: 공고 번호
     * @return
     */
    public LikeDto deleteBookmark(Long userId, String desertionNo) {
        userRepository.findById(userId).orElseThrow(()
                -> new IllegalArgumentException("사용자 권한이 없습니다."));

        LikeIt likeIt = likeRepository.findByUser_IdAndAnimal_DesertionNo(userId, desertionNo).orElseThrow(()
                -> new IllegalArgumentException("찜 내역이 없습니다."));

        likeRepository.delete(likeIt);

        return LikeDto.builder()
                .isLiked(false)
                .likeCount(likeRepository.countByAnimal_DesertionNo(desertionNo))
                .build();
    }
}