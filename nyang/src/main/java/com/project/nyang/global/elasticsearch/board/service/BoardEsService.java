package com.project.nyang.global.elasticsearch.board.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.project.nyang.global.searchlog.dto.SearchLogMessage;
import com.project.nyang.global.elasticsearch.board.dto.BoardEsDocument;
import com.project.nyang.global.elasticsearch.board.dto.BoardListDTO;
import com.project.nyang.global.elasticsearch.board.dto.LostBoardListDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * ElasticSearch 관련 Service
 *
 * @author : 박세정
 * @fileName : BoardEsService
 * @since : 2025-07-15
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BoardEsService {
    private final ElasticsearchClient client;
    private final KafkaTemplate<String, SearchLogMessage> kafkaTemplate;

    public Page<BoardListDTO> searchBoard(Long categoryId, String keyword, int page, int size) {
        sendSearchLog(keyword);

        try {
            Query query = buildBoardQuery(categoryId, keyword);
            SearchResponse<BoardEsDocument> response = executeSearch(query, page, size);

            List<BoardListDTO> boardList = new ArrayList<>();
            for (BoardEsDocument doc : extractDocuments(response)) {
                Long docCategoryId = doc.getCategoryId();
                if (docCategoryId != null && (docCategoryId == 2L || docCategoryId == 3L)) {
                    boardList.add(BoardEsDocument.toBoardDTO(doc));
                }
            }

            long total = response.hits().total().value();
            return new PageImpl<>(boardList, PageRequest.of(page, size), total);

        } catch (Exception e) {
            throw new RuntimeException("검색 중 오류 발생", e);
        }
    }

    public Page<LostBoardListDTO> searchLostBoard(Long categoryId, String keyword, int page, int size) {
        sendSearchLog(keyword);

        try {
            Query query = buildLostBoardQuery(categoryId, keyword);
            SearchResponse<BoardEsDocument> response = executeSearch(query, page, size);

            List<LostBoardListDTO> lostBoardList = new ArrayList<>();
            for (BoardEsDocument doc : extractDocuments(response)) {
                Long docCategoryId = doc.getCategoryId();
                if (docCategoryId != null && docCategoryId == 4L) {
                    lostBoardList.add(BoardEsDocument.toLostBoardDTO(doc));
                }
            }

            long total = response.hits().total().value();
            return new PageImpl<>(lostBoardList, PageRequest.of(page, size), total);

        } catch (Exception e) {
            throw new RuntimeException("검색 중 오류 발생", e);
        }
    }

    private void sendSearchLog(String keyword) {
        String searchedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        SearchLogMessage message = new SearchLogMessage(keyword, searchedAt);
        kafkaTemplate.send("search-log", message).thenAccept(result ->
                log.info("Search log sent: {}", message)
        ).exceptionally(ex -> {
            log.warn("Failed to send search log: {}", ex.getMessage());
            return null;
        });
    }

    private Query buildBoardQuery(Long categoryId, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return MatchAllQuery.of(m -> m)._toQuery();
        }

        return BoolQuery.of(b -> {
            if (categoryId != null) {
                b.filter(f -> f.term(t -> t.field("categoryId").value(String.valueOf(categoryId))));
            }
            b.should(PrefixQuery.of(p -> p.field("boardTitle").value(keyword))._toQuery());
            b.should(PrefixQuery.of(p -> p.field("boardContent").value(keyword))._toQuery());

            b.should(MatchQuery.of(p -> p.field("boardTitle.ngram").query(keyword))._toQuery());
            b.should(MatchQuery.of(p -> p.field("boardContent.ngram").query(keyword))._toQuery());

            if (keyword.length() >= 3) {
                b.should(MatchQuery.of(m -> m.field("boardTitle").query(keyword).fuzziness("AUTO"))._toQuery());
                b.should(MatchQuery.of(m -> m.field("boardContent").query(keyword).fuzziness("AUTO"))._toQuery());
            }
            b.minimumShouldMatch("1");
            return b;
        })._toQuery();
    }

    private Query buildLostBoardQuery(Long categoryId, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return MatchAllQuery.of(m -> m)._toQuery();
        }

        return BoolQuery.of(b -> {
            if (categoryId != null) {
                b.filter(f -> f.term(t -> t.field("categoryId").value(String.valueOf(categoryId))));
            }
            b.should(PrefixQuery.of(p -> p.field("kindName").value(keyword))._toQuery());
            b.should(PrefixQuery.of(p -> p.field("missingLocation").value(keyword))._toQuery());
            b.should(PrefixQuery.of(p -> p.field("boardContent").value(keyword))._toQuery());
            b.should(PrefixQuery.of(p -> p.field("distinctFeatures").value(keyword))._toQuery());

            b.should(MatchQuery.of(p -> p.field("kindName.ngram").query(keyword))._toQuery());
            b.should(MatchQuery.of(p -> p.field("missingLocation.ngram").query(keyword))._toQuery());
            b.should(MatchQuery.of(p -> p.field("boardContent.ngram").query(keyword))._toQuery());
            b.should(MatchQuery.of(p -> p.field("distinctFeatures.ngram").query(keyword))._toQuery());

            if (keyword.length() >= 3) {
                b.should(MatchQuery.of(m -> m.field("kindName").query(keyword).fuzziness("AUTO"))._toQuery());
                b.should(MatchQuery.of(m -> m.field("missingLocation").query(keyword).fuzziness("AUTO"))._toQuery());
                b.should(MatchQuery.of(m -> m.field("boardContent").query(keyword).fuzziness("AUTO"))._toQuery());
                b.should(MatchQuery.of(m -> m.field("distinctFeatures").query(keyword).fuzziness("AUTO"))._toQuery());
            }
            b.minimumShouldMatch("1");
            return b;
        })._toQuery();
    }

    private SearchResponse<BoardEsDocument> executeSearch(Query query, int page, int size) throws Exception {
        int from = page * size;
        SearchRequest request = SearchRequest.of(s -> s
                .index("board-index")
                .from(from)
                .size(size)
                .query(query)
                .sort(sort -> sort.field(f -> f.field("createdAt").order(SortOrder.Desc)))
        );
        return client.search(request, BoardEsDocument.class);
    }

    private List<BoardEsDocument> extractDocuments(SearchResponse<BoardEsDocument> response) {
        return response.hits().hits().stream()
                .map(Hit::source)
                .toList();
    }
}
