package com.project.nyang.global.elasticsearch.animal.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.project.nyang.global.elasticsearch.animal.dto.AnimalEsDocument;
import com.project.nyang.global.elasticsearch.animal.dto.AnimalEsListDTO;
import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.global.searchlog.dto.SearchLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


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

    //통합 검색 (검색어 + 필터)
    public Page<AnimalEsListDTO> searchWithKeywordAndFilter(String keyword, LocalDate startDate, LocalDate endDate, String upKindCd, String kindCd, String regionCode, String subRegionCode, PageRequest pageable) {
        String searchedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        SearchLogMessage message = new SearchLogMessage(keyword, searchedAt);

        CompletableFuture<?> future = kafkaTemplate.send("search-log", message);

        future.thenAccept(result -> log.info("Search log sent: {}", message))
                .exceptionally(ex -> {
                    log.warn("Failed to send search log: {}", ex.getMessage());
                    return null;
                });

        try {
            int from = pageable.getPageNumber() * pageable.getPageSize();
            int size = pageable.getPageSize();

            List<Query> filters = new ArrayList<>();

            //날짜 선택 유효성 검사
            if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
                throw new CustomException(ErrorCode.INVALID_NOTICE_DATE);
            }
            if ((startDate == null && endDate != null) || (startDate != null && endDate == null)) {
                throw new CustomException(ErrorCode.INVALID_DATE);
            }

            // 날짜 필터
            if (startDate != null && endDate != null) {
                filters.add(RangeQuery.of(r -> r
                        .field("noticeSdt")
                        .lte(JsonData.of(endDate.format(formatter)))
                )._toQuery());

                filters.add(RangeQuery.of(r -> r
                        .field("noticeEdt")
                        .gte(JsonData.of(startDate.format(formatter)))
                )._toQuery());
            }

            // 축종 코드
            if (upKindCd != null && !upKindCd.isBlank()) {
                filters.add(TermQuery.of(t -> t.field("upKindCd.keyword").value(upKindCd))._toQuery());
            }

            //품종 선택 유효성 검사
            if ((upKindCd == null && kindCd != null)) {
                throw new CustomException(ErrorCode.BAD_REQUEST);
            }

            // 품종 코드
            if (kindCd != null && !kindCd.isBlank()) {
                filters.add(TermQuery.of(t -> t.field("kindCd.keyword").value(kindCd))._toQuery());
            }

            // 시도 코드
            if (regionCode != null && !regionCode.isBlank()) {
                filters.add(TermQuery.of(t -> t.field("regionCode.keyword").value(regionCode))._toQuery());
            }

            //시군구 선택 유효성 검사
            if ((regionCode == null && subRegionCode != null)) {
                throw new CustomException(ErrorCode.BAD_REQUEST);
            }

            // 시군구 코드
            if (subRegionCode != null && !subRegionCode.isBlank()) {
                filters.add(TermQuery.of(t -> t.field("subRegionCode.keyword").value(subRegionCode))._toQuery());
            }

            log.info("filters: {}", filters);

            Query query;
            if (keyword == null || keyword.isBlank()) {
                query = BoolQuery.of(b -> b
                        .filter(filters)
                )._toQuery();
            } else {
                query = BoolQuery.of(b -> b
                        .should(TermQuery.of(t -> t
                                .field("noticeNo")
                                .value(keyword)
                        )._toQuery())
                        .should(MultiMatchQuery.of(m -> m
                                .query(keyword)
                                .fields(
                                        "specialMark",
                                        "happenPlace",
                                        "kindFullNm",
                                        "colorCd",
                                        "careName",
                                        "regionName",
                                        "subRegionName"
                                )
                                .fuzziness("AUTO")
                        )._toQuery())
                        .filter(filters)
                )._toQuery();
            }

            log.info("Generated query: {}", query);

            SearchRequest request = SearchRequest.of(s -> s
                    .index("animal-index")
                    .from(from)
                    .size(size)
                    .query(query)
            );

            SearchResponse<AnimalEsDocument> response = client.search(request, AnimalEsDocument.class);

            List<AnimalEsDocument> content = response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

            long total = response.hits().total() != null ? response.hits().total().value() : 0L;

            List<AnimalEsListDTO> animalEsListDTOS = new ArrayList<>();
            for (AnimalEsDocument doc : content) {
                animalEsListDTOS.add(AnimalEsDocument.toAnimalEsDTO(doc));
            }

            return new PageImpl<>(animalEsListDTOS, pageable, total);

        } catch (Exception e) {
            if (e instanceof CustomException) {
                throw (CustomException) e;
            }
            throw new RuntimeException("통합 검색 중 오류 발생", e);
        }
    }
}