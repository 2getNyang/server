package com.project.nyang.modules.board.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.project.nyang.modules.board.elasticsearch.dto.BoardEsDocument;
import com.project.nyang.modules.board.elasticsearch.dto.BoardListDTO;
import com.project.nyang.modules.board.elasticsearch.repository.BoardEsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ElasticSearch 관련 Service
 *
 * @author : 박세정
 * @fileName : BoardEsService
 * @since : 2025-07-15
 */
@Service
@RequiredArgsConstructor
public class BoardEsService {
    // Elastic search에 명령을 전달하는 서버 API
    private final ElasticsearchClient client;

    private final BoardEsRepository repository;

    public Page<BoardListDTO> searchBoard(Long categoryId, String keyword, int page, int size) {
        try {
            int from = page * size;

            Query query;

            if (keyword == null || keyword.isBlank()) {
                query = MatchAllQuery.of(m -> m)._toQuery(); // 전체 문서를 가져오는 쿼리를 생성하는 람다 함수
            }
            // 검색어가 있을 때
            else {
                query = BoolQuery.of(b -> {

                    b.filter(f -> f
                            .term(t -> t
                                    .field("categoryId")
                                    .value(String.valueOf(categoryId)))
                    );

                    // 접두어 글자 검색
                    b.should(PrefixQuery.of(p -> p.field("boardTitle").value(keyword))._toQuery());
                    b.should(PrefixQuery.of(p -> p.field("boardContent").value(keyword))._toQuery());

                    // 중간 문자 검색 (match만 가능)
                    b.should(MatchQuery.of(p -> p.field("boardTitle.ngram").query(keyword))._toQuery());
                    b.should(MatchQuery.of(p -> p.field("boardContent.ngram").query(keyword))._toQuery());

                    if (keyword.length() >= 3) {
                        b.should(MatchQuery.of(m -> m.field("boardTitle").query(keyword).fuzziness("AUTO"))._toQuery());
                        b.should(MatchQuery.of(m -> m.field("boardContent").query(keyword).fuzziness("AUTO"))._toQuery());
                    }

                    return b;
                })._toQuery();
            }

            SearchRequest request = SearchRequest.of(s -> s
                    .index("board-index")
                    .from(from)
                    .size(size)
                    .query(query)

                    .sort(sort -> sort
                            .field(f -> f
                                    .field("createdAt")
                                    .order(SortOrder.Desc)
                            )
                    )
            );

            // SearchResponse는 엘라스틱서치의 검색 결과를 담고 있는 응답 객체
            SearchResponse<BoardEsDocument> response =
                    // 엘라스틱서치에 명령을 전달하는 자바 API 검색요청을 담아서 응답객체로 반환
                    client.search(request, BoardEsDocument.class);

            // 위 응답객체에서 받은 검색 결과 중 문서만 추출해서 리스트로 만들어줌
            // Hit는 엘라스틱서치에서 검색된 문서 1개를 감싸고 있는 객체
            List<BoardEsDocument> content = response.hits() // 엘라스틱서치 응답에서 hits(문서 검색결과) 전체를 꺼냄
                    .hits() // 검색 결과 안에 개별 리스트를 가져옴
                    .stream() // JAVA stream api를 사용
                    .map(Hit::source) // 각 Hit 객체에서 실제 문서를 꺼내는 작업
                    .toList(); // 위에서 꺼낸 객체를 JAVA List에 넣는다


            // 전체 검색 결과 수 (총 문서의 갯수)
            long total = response.hits().total().value();

            List<BoardListDTO> boardList = content.stream().map(BoardEsDocument::toBoardDTO).collect(Collectors.toList());

            // PageImpl 객체를 사용해서 Spring에서 사용할 수 있는 page 객체로 변환
            return new PageImpl<>(boardList, PageRequest.of(page, size), total);

        } catch (Exception e) {
            throw new RuntimeException("검색 중 오류 발생", e);
        }
    }
    
}