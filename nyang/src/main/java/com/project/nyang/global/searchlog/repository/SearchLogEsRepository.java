package com.project.nyang.global.searchlog.repository;

import com.project.nyang.global.searchlog.domain.SearchLogDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
/**
 *
 * 엘라스틱 서치 저장/검색용 레포지토리
 * @fileName        : SearchLogEsRepository
 * @author          : 박세정
 * @since           : 2025-07-16
 *
 */
public interface SearchLogEsRepository extends ElasticsearchRepository<SearchLogDocument,String> {
}
