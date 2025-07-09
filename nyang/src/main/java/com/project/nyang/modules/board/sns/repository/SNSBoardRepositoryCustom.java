package com.project.nyang.modules.board.sns.repository;

/**
 * sns repository
 *
 * @author : 이은서
 * @fileName : SNSBoardRepositoryCustom
 * @since : 25. 7. 9.
 */
public interface SNSBoardRepositoryCustom {
    void updateSNSBoard(Long boardId, String title, String content, String instagramLink);
}
