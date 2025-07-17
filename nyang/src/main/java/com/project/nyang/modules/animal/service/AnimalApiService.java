package com.project.nyang.modules.animal.service;

import com.project.nyang.global.common.publicapi.PublicAnimalApiClient;
import com.project.nyang.modules.animal.dto.AnimalApiResponse;
import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.animal.repository.AnimalRepository;
import com.project.nyang.modules.shelter.entity.Shelter;
import com.project.nyang.modules.shelter.repository.ShelterRepository;
import com.project.nyang.reference.entity.Kind;
import com.project.nyang.reference.entity.UpKind;
import com.project.nyang.reference.repository.KindRepository;
import com.project.nyang.reference.repository.UpkindRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * AnimalApiService입니다.
 *
 * @author : 엄아영
 * @fileName : AnimalApiService
 * @since : 2025-07-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalApiService {

    private final PublicAnimalApiClient publicAnimalApiClient;
    private final AnimalRepository animalRepository;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private  final DateTimeFormatter updTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    private final ShelterRepository shelterRepository;
    private final UpkindRepository upkindRepository;
    private final KindRepository kindRepository;

    //api 불러오는지 test
    @Transactional
    public void testFetchAnimals(String startDate, String endDate) {
        List<AnimalApiResponse> apiResponses = publicAnimalApiClient.fetchAnimals(startDate, endDate, 1, 1000);

        for (AnimalApiResponse response : apiResponses) {
            System.out.println("응답 데이터: " + response);
        }
    }

    //최초 16일치 데이터 가져오기
    @Transactional
    public void initialize() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(15);
        String startDateStr = startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String endDateStr = endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        fetchAndSaveAnimals(startDateStr, endDateStr);
    }

    //불러와서 animal에 저장
    @Transactional
    public void fetchAndSaveAnimals(String startDate, String endDate) {
        int pageNo = 1;
        int numOfRows = 1000;
        boolean hasMoreData = true;

        // Shelter, UpKind, Kind 캐싱
        Map<String, Shelter> shelterMap = shelterRepository.findAll().stream()
                .collect(Collectors.toMap(Shelter::getCareRegNumber, Function.identity()));

        Map<String, UpKind> upKindMap = upkindRepository.findAll().stream()
                .collect(Collectors.toMap(UpKind::getUpKindCd, Function.identity()));

        Map<String, Kind> kindMap = kindRepository.findAll().stream()
                .collect(Collectors.toMap(Kind::getKindCd, Function.identity()));

        while (hasMoreData) {
            List<AnimalApiResponse> apiResponses = publicAnimalApiClient.fetchAnimals(startDate, endDate, pageNo, numOfRows);

            if (apiResponses.isEmpty()) {
                hasMoreData = false;
                break;
            }

            // API 응답에서 desertionNo Set 생성
            Set<String> incomingDesertionNos = apiResponses.stream()
                    .map(AnimalApiResponse::getDesertionNo)
                    .collect(Collectors.toSet());

            // DB에서 desertionNo 일괄 조회
            Map<String, Animal> existingAnimalsMap = animalRepository.findByDesertionNoIn(incomingDesertionNos)
                    .stream()
                    .collect(Collectors.toMap(Animal::getDesertionNo, Function.identity()));

            List<Animal> animalsToSave = new ArrayList<>();

            for (AnimalApiResponse apiResponse : apiResponses) {
                Animal newAnimal = convertToAnimalEntity(apiResponse, shelterMap, upKindMap, kindMap);
                if (newAnimal == null) continue;

                Animal existingAnimal = existingAnimalsMap.get(apiResponse.getDesertionNo());
                if (existingAnimal != null) {
                    existingAnimal.updateFrom(newAnimal); // 수정
                    animalsToSave.add(existingAnimal);
                } else {
                    animalsToSave.add(newAnimal); // 신규
                }
            }

            if (!animalsToSave.isEmpty()) {
                animalRepository.saveAll(animalsToSave);
                log.info("저장 및 수정 {} animals to DB on page {}", animalsToSave.size(), pageNo);
            }

            if (apiResponses.size() < numOfRows) {
                hasMoreData = false;
            } else {
                pageNo++;
            }
        }
    }

    //업데이트
    @Transactional
    public void updateAnimals(String startDate, String endDate) {
        int pageNo = 1;
        int numOfRows = 1000;
        boolean hasMoreData = true;

        //db에서 가장 오래된 발견일 날짜 조회
        LocalDate oldestDate = animalRepository.findOldestHappenDt();
        if (oldestDate == null) {
            oldestDate = LocalDate.MIN; // DB에 아무 데이터가 없는 경우
        }

        // Shelter, UpKind, Kind 캐싱
        Map<String, Shelter> shelterMap = shelterRepository.findAll().stream()
                .collect(Collectors.toMap(Shelter::getCareRegNumber, Function.identity()));

        Map<String, UpKind> upKindMap = upkindRepository.findAll().stream()
                .collect(Collectors.toMap(UpKind::getUpKindCd, Function.identity()));

        Map<String, Kind> kindMap = kindRepository.findAll().stream()
                .collect(Collectors.toMap(Kind::getKindCd, Function.identity()));

        while (hasMoreData) {
            List<AnimalApiResponse> apiResponses = publicAnimalApiClient.updateAnimals(startDate, endDate, pageNo, numOfRows);

            if (apiResponses.isEmpty()) {
                hasMoreData = false;
                break;
            }

            // API 응답에서 desertionNo Set 생성
            Set<String> incomingDesertionNos = apiResponses.stream()
                    .map(AnimalApiResponse::getDesertionNo)
                    .collect(Collectors.toSet());

            // DB에서 desertionNo 일괄 조회
            Map<String, Animal> existingAnimalsMap = animalRepository.findByDesertionNoIn(incomingDesertionNos)
                    .stream()
                    .collect(Collectors.toMap(Animal::getDesertionNo, Function.identity()));

            List<Animal> animalsToSave = new ArrayList<>();

            for (AnimalApiResponse apiResponse : apiResponses) {
                String happenDtStr = apiResponse.getHappenDt();
                LocalDate happenDt = LocalDate.parse(happenDtStr, dateTimeFormatter);
                if (happenDt.isBefore(oldestDate)) {
                    continue; // 과거 데이터면 skip
                }

                Animal newAnimal = convertToAnimalEntity(apiResponse, shelterMap, upKindMap, kindMap);
                if (newAnimal == null) continue;

                Animal existingAnimal = existingAnimalsMap.get(apiResponse.getDesertionNo());
                if (existingAnimal != null) {
                    existingAnimal.updateFrom(newAnimal); // 수정
                    animalsToSave.add(existingAnimal);
                } else {
                    animalsToSave.add(newAnimal); // 신규
                }
            }

            if (!animalsToSave.isEmpty()) {
                animalRepository.saveAll(animalsToSave);
                log.info("업데이트 {} animals to DB on page {}", animalsToSave.size(), pageNo);
            }

            if (apiResponses.size() < numOfRows) {
                hasMoreData = false;
            } else {
                pageNo++;
            }
        }
    }

    // AnimalEntity로 변환
    private Animal convertToAnimalEntity(AnimalApiResponse response,
                                         Map<String, Shelter> shelterMap,
                                         Map<String, UpKind> upKindMap,
                                         Map<String, Kind> kindMap) {

        Shelter shelter = shelterMap.get(response.getCareRegNo());
        if (shelter == null) {
            log.warn("Shelter not found: {}", response.getCareRegNo());
            return null;
        }

        UpKind upKind = upKindMap.get(response.getUpKindCd());
        if (upKind == null) {
            log.warn("UpKind not found: {}", response.getUpKindCd());
            return null;
        }

        Kind kind = kindMap.get(response.getKindCd());
        if (kind == null) {
            log.warn("Kind not found: {}", response.getKindCd());
            return null;
        }

        return Animal.builder()
                .desertionNo(response.getDesertionNo())
                .shelter(shelter)
                .upKind(upKind)
                .kind(kind)
                .upKindCd(upKind.getUpKindCd())
                .kindCd(kind.getKindCd())
                .happenDt(LocalDate.parse(response.getHappenDt(), dateTimeFormatter))
                .noticeSdt(LocalDate.parse(response.getNoticeSdt(), dateTimeFormatter))
                .noticeEdt(LocalDate.parse(response.getNoticeEdt(), dateTimeFormatter))
                .happenPlace(response.getHappenPlace())
                .kindFullNm(response.getKindFullNm())
                .colorCd(response.getColorCd())
                .age(response.getAge())
                .weight(response.getWeight())
                .noticeNo(response.getNoticeNo())
                .popfile1(response.getPopfile1())
                .popfile2(response.getPopfile2())
                .popfile3(response.getPopfile3())
                .processState(response.getProcessState())
                .sexCd(response.getSexCd())
                .neuterYn(response.getNeuterYn())
                .specialMark(response.getSpecialMark())
                .updTm(LocalDateTime.parse(response.getUpdTm(), updTimeFormatter))
                .build();
    }

    //오래된 동물 정보 삭제
    @Transactional
    public void deleteOldestAnimals() {
        // 가장 오래된 happenDt 조회
        LocalDate oldestDate = animalRepository.findOldestHappenDt();
        if (oldestDate == null) {
            log.info("삭제할 오래된 데이터가 없습니다.");
            return;
        }

        // 해당 happenDt를 가진 Animal 모두 조회
        List<Animal> oldestAnimals = animalRepository.findAllByHappenDt(oldestDate);

        // 삭제
        animalRepository.deleteAll(oldestAnimals);
        log.info("Deleted {} animals with happenDt: {}", oldestAnimals.size(), oldestDate);
    }



}