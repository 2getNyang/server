package com.project.nyang.modules.board.elasticsearch.repository;

import com.project.nyang.modules.board.elasticsearch.dto.BoardEsDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * ElasticSearch 관련 repository
 *
 * @author : 박세정
 * @fileName : BoardEsRepository
 * @since : 2025-07-15
 */
@Repository
public interface BoardEsRepository  extends ElasticsearchRepository<BoardEsDocument,String> {
}
