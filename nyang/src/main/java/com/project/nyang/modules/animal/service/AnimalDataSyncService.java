package com.project.nyang.modules.animal.service;

import com.project.nyang.global.elasticsearch.animal.dto.AnimalEsDocument;
import com.project.nyang.global.elasticsearch.animal.repository.AnimalEsRepository;
import com.project.nyang.modules.animal.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * AnimalDataSyncService
 *
 * @author : 엄아영
 * @fileName : AnimalDataSyncService
 * @since : 2025-07-26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnimalDataSyncService {

    private final AnimalRepository animalRepository;
    private final AnimalEsRepository animalEsRepository;

    /**
     * DB와 ES 간 desertionNo 정합성 비교 결과 로그 출력 및 리턴
     */
    @Transactional(readOnly = true)
    public Map<String, Object> compareDbAndEsIndex() {

        // 1. DB에서 desertionNo 전체 조회
        Set<String> dbIds = new HashSet<>(animalRepository.findAllDesertionNos());

        // 2. ES에서 desertionNo 전체 조회
        Set<String> esIds = StreamSupport.stream(animalEsRepository.findAll().spliterator(), false)
                .map(AnimalEsDocument::getDesertionNo)
                .collect(Collectors.toSet());

        // 3. 비교
        Set<String> onlyInDb = new HashSet<>(dbIds);
        onlyInDb.removeAll(esIds); // ES에 누락된 desertionNo

        Set<String> onlyInEs = new HashSet<>(esIds);
        onlyInEs.removeAll(dbIds); // DB에는 없고 ES에만 있는 desertionNo

        log.info("✅ DB 개수: {}", dbIds.size());
        log.info("✅ ES 개수: {}", esIds.size());
        log.warn("❗ DB에는 있고 ES에는 없는 수: {}", onlyInDb.size());
        log.warn("❗ ES에는 있고 DB에는 없는 수: {}", onlyInEs.size());

        // 결과 리턴 (API 응답으로 활용 가능)
        Map<String, Object> result = new HashMap<>();
        result.put("dbCount", dbIds.size());
        result.put("esCount", esIds.size());
        result.put("onlyInDbCount", onlyInDb.size());
        result.put("onlyInEsCount", onlyInEs.size());
        result.put("onlyInDb", onlyInDb); // 필요시 삭제
        result.put("onlyInEs", onlyInEs); // 필요시 삭제
        return result;
    }
}
