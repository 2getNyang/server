package com.project.nyang.modules.animal.service;

import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.global.logging.LogMessage;
import com.project.nyang.modules.animal.dto.AnimalDTO;
import com.project.nyang.modules.animal.dto.AnimalDashboardDTO;
import com.project.nyang.modules.animal.dto.AnimalListDTO;
import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.animal.repository.AnimalRepository;
import com.project.nyang.modules.comment.dto.AnimalCommentDTO;
import com.project.nyang.modules.comment.entity.Comment;
import com.project.nyang.modules.comment.repository.CommentRepository;
import com.project.nyang.modules.like.repository.LikeRepository;
import com.project.nyang.modules.shelter.entity.Shelter;
import com.project.nyang.modules.shelter.repository.ShelterRepository;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import com.project.nyang.reference.dto.RegionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * AnimalService입니다
 *
 * @author : 엄아영, 이지은
 * @fileName : AnimalService
 * @since : 2025-07-09
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;

    //페이징 전체 목록
    @Transactional
    public Page<AnimalListDTO> getAnimals(PageRequest pageable) {
        return animalRepository.findAllAnimals(pageable); //페이저블에 페이징에대한 정보를 담아서 레포지토리에 전달하는 역할
    }

    //페이징 동물 필터 검색
    @LogMessage(value = "페이징 동물 필터 검색", operation = "READ")
    @Transactional
    public Page<AnimalListDTO> getFilterAnimals(LocalDate startDate, LocalDate endDate,
                                                String upKindCd, String kindCd,
                                                String regionCode, String subRegionCode,
                                                PageRequest pageable) {

        //시작일과 종료일이 선택되지 않으면 예외처리
        //프론트에서도 막아둬야함
        if(startDate == null || endDate == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        //사용자가 선택한 시작 날짜가 종료일보다 이전이어야함
        if (startDate.isAfter(endDate)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        // 축종이 null인데 품종 값이 들어왔을 경우 예외 처리
        if(upKindCd == null && kindCd != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        // 시도가 null인데 시군구 값이 들어왔을 경우 예외 처리
        if(regionCode == null && subRegionCode != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        return animalRepository.getFilterAnimals(startDate, endDate, upKindCd, kindCd, regionCode, subRegionCode, pageable);
    }

    //Animal 상세 조회
    @LogMessage(value = "유기동물 정보 상세 조회", operation = "READ")
    public AnimalDTO getAnimalDetail(String desertionNo,  Long userId) {

        Animal animal = animalRepository.findByDesertionNoWithShelter(desertionNo)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_ANIMAL));

        // 댓글 조회
        List<Comment> comments = commentRepository.findByAnimal_DesertionNo(desertionNo);
        List<AnimalCommentDTO> commentDTOs = comments.stream()//리스트 안의 요소들을 하나씩 치러할수있는 파이프라인을 만듬
                .map(AnimalCommentDTO::fromEntity)//스트림의 각 요소를 함수로 변환  (Comment 객체를 CommentDTO로 변환)
                .toList(); //리스트로 수집함  (최종적으로 List<CommentDTO>를 얻음)

        //북마크 여부
        //userId가 null이면 좋아요 여부는 false
        boolean bookmarked = false;
        //userId가 null이 아닐 경우 북마크 조회
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            bookmarked = likeRepository.existsByUserAndAnimal(user, animal);
        }

        // DTO 변환 + 댓글 포함 + 좋아요 포함
        return toDTO(animal, commentDTOs, bookmarked);
    }

    // Entity → DTO 변환
    private AnimalDTO toDTO(Animal animal, List<AnimalCommentDTO> comments, boolean bookmarked) {
        Shelter shelter = animal.getShelter();
        return AnimalDTO.builder()
                .desertionNo(animal.getDesertionNo())
                .happenDt(animal.getHappenDt())
                .happenPlace(animal.getHappenPlace())
                .kindFullNm(animal.getKindFullNm())
                .colorCd(animal.getColorCd())
                .age(animal.getAge())
                .weight(animal.getWeight())
                .noticeNo(animal.getNoticeNo())
                .noticeSdt(animal.getNoticeSdt())
                .noticeEdt(animal.getNoticeEdt())
                .popfile1(animal.getPopfile1())
                .popfile2(animal.getPopfile2())
                .popfile3(animal.getPopfile3())
                .processState(animal.getProcessState())
                .sexCd(animal.getSexCd())
                .neuterYn(animal.getNeuterYn())
                .specialMark(animal.getSpecialMark())
                .comments(comments)
                .bookmarked(bookmarked)   // 여기에 추가
                .shelterName(shelter.getCareName())
                .shelterAddress(shelter.getCareAddress())
                .shelterTel(shelter.getCareTel())
                .careRegNumber(shelter.getCareRegNumber())
                .build();
    }

    @Transactional
    public List<AnimalListDTO> getRecommendAnimals() {
        return animalRepository.findRecommendAnimals(PageRequest.of(0, 6));
    }

    public AnimalDashboardDTO getDashboardCounts() {
        return AnimalDashboardDTO.builder()
                .shelterCount(shelterRepository.count())    //보호소 갯수
                .protectedAnimalCount(animalRepository.countByProcessStateContaining("보호중"))    //보호중 공고 갯수
                //입양완료된 공고 갯수
                .adoptedOrReturnedCount(animalRepository.countByProcessStateContainingOrProcessStateContaining("반환", "입양"))
                .build();
    }
}