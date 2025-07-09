
package com.project.nyang.modules.board.sns.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SNSBoardRepositoryImpl implements SNSBoardRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void updateSNSBoard(Long boardId, String title, String content, String instagramLink) {
        em.createQuery("UPDATE Board b SET b.boardTitle = :title, b.boardContent = :content, b.instagramLink = :link WHERE b.id = :boardId")
                .setParameter("title", title)
                .setParameter("content", content)
                .setParameter("link", instagramLink)
                .setParameter("boardId", boardId)
                .executeUpdate();
    }
}
