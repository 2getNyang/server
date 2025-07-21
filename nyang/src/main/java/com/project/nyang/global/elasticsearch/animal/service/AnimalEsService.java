package com.project.nyang.global.elasticsearch.animal.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.project.nyang.global.elasticsearch.animal.dto.AnimalEsDocument;
import com.project.nyang.global.elasticsearch.animal.dto.AnimalEsListDTO;
import com.project.nyang.global.elasticsearch.board.dto.BoardEsDocument;
import com.project.nyang.global.elasticsearch.board.dto.BoardListDTO;
import com.project.nyang.global.elasticsearch.board.dto.LostBoardListDTO;
import com.project.nyang.global.searchlog.dto.SearchLogMessage;
import com.project.nyang.modules.animal.dto.AnimalListDTO;
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
public class AnimalEsService {
    // Elastic search에 명령을 전달하는 서버 API
    private final ElasticsearchClient client;
    private final KafkaTemplate<String, SearchLogMessage> kafkaTemplate;

    public Page<? extends AnimalEsListDTO> searchEsAnimals(String keyword, int page, int size) {
        String searchedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        SearchLogMessage message = new SearchLogMessage(keyword, searchedAt);

        CompletableFuture<?> future = kafkaTemplate.send("search-log", message);

        future.thenAccept(result -> log.info("Search log sent: {}", message))
                .exceptionally(ex -> {
                    log.warn("Failed to send search log: {}", ex.getMessage());
                    return null;
                });



        try {
            int from = page * size;

            Query query;

            if (keyword == null || keyword.isBlank()) {
                query = MatchAllQuery.of(m -> m)._toQuery(); // 전체 문서를 가져오는 쿼리를 생성하는 람다 함수
            }
            // 검색어가 있을 때
            else {
                query = BoolQuery.of(b -> b
                        .should(TermQuery.of(t -> t
                                .field("noticeNo.keyword")
                                .value(keyword)
                        )._toQuery())
                        .should(MultiMatchQuery.of(m -> m
                                .query(keyword)
                                .fields(
                                        "specialMark",      //특이사항
                                        "happenPlace",    //발견 장소
                                        "kindFullNm",            //축종 품종 풀네임
                                        "colorCd",               //털 색깔
                                        "careName",              //보호소 이름
                                        "regionName",            //시/도
                                        "subRegionName"         //시/군/구
                                )
                                .fuzziness("AUTO")  // 오타나도 유의어 검색해줌
                        )._toQuery())
                )._toQuery();
            }

            SearchRequest request = SearchRequest.of(s -> s
                    .index("animal-index")
                    .from(from)
                    .size(size)
                    .query(query)
            );

            // SearchResponse는 엘라스틱서치의 검색 결과를 담고 있는 응답 객체
            SearchResponse<AnimalEsDocument> response =
                    // 엘라스틱서치에 명령을 전달하는 자바 API 검색요청을 담아서 응답객체로 반환
                    client.search(request, AnimalEsDocument.class);

            // 위 응답객체에서 받은 검색 결과 중 문서만 추출해서 리스트로 만들어줌
            // Hit는 엘라스틱서치에서 검색된 문서 1개를 감싸고 있는 객체
            List<AnimalEsDocument> content = response.hits() // 엘라스틱서치 응답에서 hits(문서 검색결과) 전체를 꺼냄
                    .hits() // 검색 결과 안에 개별 리스트를 가져옴
                    .stream() // JAVA stream api를 사용
                    .map(Hit::source) // 각 Hit 객체에서 실제 문서를 꺼내는 작업
                    .toList(); // 위에서 꺼낸 객체를 JAVA List에 넣는다


            // 전체 검색 결과 수 (총 문서의 갯수)
            long total = response.hits().total().value();

            List<AnimalEsListDTO> animalEsListDTOS = new ArrayList<>();

            for (AnimalEsDocument doc : content) {
                animalEsListDTOS.add(AnimalEsDocument.toAnimalEsDTO(doc));
            }

            // PageImpl 객체 반환 (categoryId에 따라 분기)
            return new PageImpl<>(animalEsListDTOS, PageRequest.of(page, size), total);

        } catch (Exception e) {
            throw new RuntimeException("검색 중 오류 발생", e);
        }
    }
}