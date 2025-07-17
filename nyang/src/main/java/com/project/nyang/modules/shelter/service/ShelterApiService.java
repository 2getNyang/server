package com.project.nyang.modules.shelter.service;

import com.project.nyang.global.common.publicapi.PublicShelterApiClient;
import com.project.nyang.modules.shelter.dto.ShelterApiResponse;
import com.project.nyang.modules.shelter.entity.Shelter;
import com.project.nyang.modules.shelter.repository.ShelterRepository;
import com.project.nyang.reference.entity.Region;
import com.project.nyang.reference.entity.SubRegion;
import com.project.nyang.reference.repository.RegionRepository;
import com.project.nyang.reference.repository.SubRegionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * ShelterApiService에 대한 클래스입니다.
 *
 * @author : 오승훈
 * @fileName : ShelterApiService
 * @since : 2025-07-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShelterApiService {

    private final PublicShelterApiClient publicShelterApiClient;
    private final ShelterRepository shelterRepository;
    private final RegionRepository regionRepository;
    private final SubRegionRepository subRegionRepository;
    private final DateTimeFormatter dataStdDtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Transactional
    public void fetchAndUpdateShelters() {
        log.info("📥 보호소 API 수집 시작");
        List<ShelterApiResponse> apiResponses = publicShelterApiClient.fetchShelters();

        // DB에 있는 모든 Shelter 캐싱
        Map<String, Shelter> shelterMap = shelterRepository.findAll().stream()
                .collect(Collectors.toMap(Shelter::getCareRegNumber, Function.identity()));

        int newCount = 0, updateCount = 0, skipCount = 0;
        LocalDate now = LocalDate.now();

        for (ShelterApiResponse dto : apiResponses) {
            String careRegNo = dto.getCareRegNo();
            Shelter existing = shelterMap.get(careRegNo);

            Shelter converted = convertToShelter(dto);
            if (converted == null) {
                skipCount++;
                continue;
            }

            if (existing == null) {
                shelterRepository.save(converted);
                shelterMap.put(careRegNo, converted);
                log.info("✅ 신규 보호소 저장됨: {}", converted.getCareName());
                newCount++;
            } else {
                String stdDtStr = dto.getDataStdDt();
                if (stdDtStr == null || stdDtStr.isBlank()) {
                    log.warn("❌ [갱신 스킵 - 데이터 기준일자 누락] 보호소: {}, 번호: {}", existing.getCareName(), careRegNo);
                    skipCount++;
                    continue;
                }

                LocalDate apiDate;
                try {
                    apiDate = LocalDate.parse(stdDtStr, dataStdDtFormatter);
                } catch (Exception e) {
                    log.warn("❌ [갱신 스킵 - 기준일자 파싱 실패] 보호소: {}, 원본 값: {}", existing.getCareName(), stdDtStr);
                    skipCount++;
                    continue;
                }

                if (apiDate.isBefore(now.minusDays(30))) {
                    log.info("ℹ️ [30일 초과 - 갱신 스킵] 보호소: {}, 번호: {}, 기준일자: {}", existing.getCareName(), careRegNo, apiDate);
                    skipCount++;
                    continue;
                }

                existing.updateFrom(converted);
                log.info("🔄 보호소 갱신됨: {}", existing.getCareName());
                updateCount++;
            }
        }

        log.info("✅ 보호소 API 처리 완료 - 신규: {}, 갱신: {}, 스킵: {}", newCount, updateCount, skipCount);
    }

    private Shelter convertToShelter(ShelterApiResponse dto) {
        if (dto.getCareRegNo() == null || dto.getCareRegNo().isBlank()) {
            log.warn("❌ 보호소 등록번호 누락, 변환 실패: {}", dto);
            return null;
        }

        String[] parts = dto.getOrgNm() == null ? new String[0] : dto.getOrgNm().trim().split("\\s+");
        String sido = parts.length > 0 ? parts[0] : null;
        String sigungu = parts.length > 1 ? parts[1] : sido;

        Region region = regionRepository.findByRegionName(sido)
                .orElseThrow(() -> new IllegalArgumentException("❌ 시/도 정보 없음: " + sido));

        List<SubRegion> subRegions = subRegionRepository.findAllBySubRegionName(sigungu);

        // 일치하는 시/도 소속 subRegion이 없으면 대체 코드 사용
        SubRegion subRegion = subRegions.stream()
                .filter(sr -> sr.getRegion().equals(region))
                .findFirst()
                .orElseGet(() -> {
                    log.warn("⚠️ 시/군/구 '{}' 매칭 안됨, '{}' 시/도 코드로 대체", sigungu, sido);
                    return subRegionRepository.findById(region.getRegionCode())
                            .orElseThrow(() -> new IllegalArgumentException("❌ 대체용 시/군/구 없음: " + region.getRegionCode()));
                });

        return Shelter.builder()
                .careRegNumber(dto.getCareRegNo())
                .careName(dto.getCareNm())
                .careAddress(dto.getCareAddr())
                .jibunAddress(dto.getJibunAddr())
                .latitude(dto.getLat() != null ? dto.getLat().floatValue() : null)
                .longitude(dto.getLng() != null ? dto.getLng().floatValue() : null)
                .careTel(dto.getCareTel())
                .careEmail(null) // 추후 확장 시 매핑해주세요
                .region(region)
                .subRegion(subRegion)
                .build();
    }
}


