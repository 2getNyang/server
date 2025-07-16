package com.project.nyang.global.common.publicapi;

import com.project.nyang.modules.shelter.dto.ShelterApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 외부 api를 호출하는 PublicShelterApiClient에 대한 클래스입니다.
 *
 * @author : 오승훈
 * @fileName : PublicShelterApiClient
 * @since : 2025-07-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PublicShelterApiClient {

    private final RestTemplate restTemplate;

    private static final String BASE_URL = "https://apis.data.go.kr/1543061/animalShelterSrvc_v2/shelterInfo_v2";

    @Value("${publicapi.service-key}")
    private String serviceKey;

    /**
     * 페이지를 반복적으로 호출하여 전체 보호소 데이터를 수집
     */
    public List<ShelterApiResponse> fetchShelters() {
        int pageNo = 1;
        int numOfRows = 1000;
        boolean hasMore = true;

        List<ShelterApiResponse> allItems = new ArrayList<>();

        while (hasMore) {
            String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("_type", "json")
                    .queryParam("pageNo", pageNo)
                    .queryParam("numOfRows", numOfRows)
                    .build()
                    .toUriString();

            log.info("🔄 보호소 API 호출 중 - page: {}", pageNo);

            try {
                ResponseEntity<PublicShelterApiResponse> response =
                        restTemplate.getForEntity(url, PublicShelterApiResponse.class);

                List<ShelterApiResponse> items = response.getBody()
                        .getResponse()
                        .getBody()
                        .getItems()
                        .getItem();

                if (items == null || items.isEmpty()) {
                    hasMore = false;
                } else {
                    allItems.addAll(items);
                    if (items.size() < numOfRows) {
                        hasMore = false;
                    } else {
                        pageNo++;
                    }
                }

            } catch (Exception e) {
                log.error("❌ 보호소 API 호출 실패: {}", e.getMessage(), e);
                hasMore = false;
            }
        }

        log.info("✅ 보호소 API 전체 수집 완료 - 총 {}건", allItems.size());
        return allItems;
    }
}
