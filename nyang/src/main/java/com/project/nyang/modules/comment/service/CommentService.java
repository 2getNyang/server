package com.project.nyang.modules.comment.service;

import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.comment.dto.CreateCommentDTO;
import com.project.nyang.modules.comment.dto.UpdateCommentDTO;
import com.project.nyang.modules.comment.entity.Comment;
import com.project.nyang.modules.comment.repository.CommentRepository;
import com.project.nyang.modules.like.repository.BoardRepository;
import com.project.nyang.modules.like.repository.TempAnimalRepository;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;

/**
 * 댓글 Service
 *
 * @author : 박세정
 * @fileName : CommentService
 * @since : 2025-07-11
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final TempAnimalRepository animalRepository;

    /**
     * 게시글 댓글 등록 메서드
     * @param userId: 사용자 ID
     * @param boardId: 게시글 ID
     * @param dto :
     */
    @Transactional
    public void createBoardComment(Long userId, Long boardId, CreateCommentDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 올바르지 않습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        Comment parent = null;
        if(dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("상위 댓글이 없습니다."));

            // 1. 상위 댓글이 게시글의 댓글이 아니거나
            // 2. 상위 댓글과 생성 댓글의 게시글 ID가 다르거나
            // 3. 상위 댓글이 이미 하위 댓글일 때
            if(parent.getBoard() == null || !parent.getBoard().equals(board) || parent.getParent() != null) {
                throw new IllegalArgumentException("상위 댓글 정보가 잘못되었습니다.");
            }
        }
        
        Comment comment = Comment.builder()
                .commentContent(dto.getCommentContent())
                .parent(parent)
                .user(user)
                .board(board)
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    public void createAdoptionComment(Long userId, String desertionNo, CreateCommentDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 올바르지 않습니다."));

        Animal animal = animalRepository.findByDesertionNo(desertionNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 공고가 없습니다."));

        Comment parent = null;
        if(dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("상위 댓글이 없습니다."));

            // 1. 상위 댓글이 입양공고의 댓글이 아니거나
            // 2. 상위 댓글과 생성 댓글의 공고번호가 다르거나
            // 3. 상위 댓글이 이미 하위 댓글일 때
            if(parent.getAnimal() ==  null || !parent.getAnimal().equals(animal) || parent.getParent() != null) {
                throw new IllegalArgumentException("상위 댓글 정보가 잘못되었습니다.");
            }
        }

        Comment comment = Comment.builder()
                .commentContent(dto.getCommentContent())
                .parent(parent)
                .user(user)
                .animal(animal)
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    public UpdateCommentDTO updateComment(Long userId, Long commentId, UpdateCommentDTO dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        System.out.println(comment.getUser().getId());
        System.out.println(userId);
        System.out.println(comment.getUser().getId().equals(userId));
        if(!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }

        comment.updateComment(dto.getCommentContent());

        return UpdateCommentDTO.builder()
                .commentContent(comment.getCommentContent())
                .build();

    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if(!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        comment.markDeleted();
    }

}