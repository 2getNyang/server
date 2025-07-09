package com.project.nyang.modules.board.SNS.service;

import com.project.nyang.modules.board.SNS.DTO.SNSBoardDTO;
import com.project.nyang.modules.board.SNS.repository.SNSBoardRepository;
import com.project.nyang.modules.board.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * snsboard service 입니다
 *
 * @author : 이은서
 * @fileName : SNSBoardService
 * @since : 25. 7. 8.
 */
@Service
@RequiredArgsConstructor
public class SNSBoardService {
    private final SNSBoardRepository snsBoardRepository;

    public SNSBoardDTO createBoard(SNSBoardDTO dto) {
        Board board = Board.builder()
                .category(dto.getCategory())
                .boardTitle(dto.getBoardTitle())
                .boardContent(dto.getBoardContent())
                .instagramLink(dto.getInstagramLink())
                .viewCount(0L)
                .build();
        Board saved = snsBoardRepository.save(board);
        return new SNSBoardDTO(saved);
    }
    /* 카테고리 : sns 홍보인 게시글 전체조회 */
    public List<SNSBoardDTO> getAllSNSBoards() {
        return snsBoardRepository.findAll().stream()
                .filter(board -> board.getCategory().getCategoryType().equals("SNS홍보")) /* 여기 카테고리유형을 어떻게 저장하냐에 따라 ID로 할지 CategoryType을 받을지 정할듯...? */
                .map(SNSBoardDTO::new)
                .collect(Collectors.toList());
    }

    /* 상세조회일까*/
//    public SNSBoardDTO getSNSBoardById(Long id) {
//        Board board = snsBoardRepository.findById(id).orElseThrow(()->new IllegalArgumentException("게시글이 존재하지않습니다."));
//        return new SNSBoardDTO(board);
//        /* 조회수 증가 */
//        //board.setViewCount(board.getViewCount()+1); /* setter를 사용해야 set ViewCount가 됩니다.. 강사님꺼도 Board Entity에 @Setter있었어요 */
//
//        return new SNSBoardDTO(board);
//    }

}
