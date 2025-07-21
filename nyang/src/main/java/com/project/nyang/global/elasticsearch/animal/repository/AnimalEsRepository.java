package com.project.nyang.global.elasticsearch.animal.repository;

import com.project.nyang.global.elasticsearch.animal.dto.AnimalEsDocument;
import com.project.nyang.global.elasticsearch.board.dto.BoardEsDocument;
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
public interface AnimalEsRepository extends ElasticsearchRepository<AnimalEsDocument,String> {
}
