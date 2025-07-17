package com.project.nyang.global.common.publicapi;

import com.project.nyang.modules.animal.dto.AnimalApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * 외부 api를 호출하는 PublicAnimalApiClient 생성합니다.
 *
 * @author : 엄아영
 * @fileName : PublicAnimalApiClient
 * @since : 2025-07-14
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicAnimalApiClient {

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "https://apis.data.go.kr/1543061/abandonmentPublicService_v2/abandonmentPublic_v2";

    @Value("${publicapi.service-key}")
    private String serviceKey;

    public List<AnimalApiResponse> fetchAnimals(String startDate, String endDate, int pageNo, int numOfRows) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("bgnde", startDate)
                .queryParam("endde", endDate)
                .queryParam("_type", "json")
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .build()
                .toUriString();
        log.info("Request URL: {}", url);

        ResponseEntity<PublicApiResponse> response = restTemplate.getForEntity(url, PublicApiResponse.class);
        return response.getBody().getResponse().getBody().getItems().getItem();
    }

    // updTm 기준으로 가져와서 수정
    public List<AnimalApiResponse> updateAnimals(String startDate, String endDate, int pageNo, int numOfRows) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("bgupd", startDate)
                .queryParam("enupd", endDate)
                .queryParam("_type", "json")
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .build()
                .toUriString();
        log.info("Request URL: {}", url);

        ResponseEntity<PublicApiResponse> response = restTemplate.getForEntity(url, PublicApiResponse.class);
        return response.getBody().getResponse().getBody().getItems().getItem();
    }


}