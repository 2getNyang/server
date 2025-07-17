package com.project.nyang.modules.adoption.constant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 입양신청서 질문 항목에 대한 열거형 파일입니다.
 *
 * @author : 이지은
 * @fileName : AdoptionQuestion
 * @since : 25. 7. 15.
 */
@Getter
@AllArgsConstructor
@Schema(description = "입양 질문")
public enum AdoptionQuestion {
    NAME("이름"),
    BIRTH("생년월일"),
    GENDER("성별"),
    PHONE("전화번호"),
    ADDRESS("주소"),
    DETAIL_ADDRESS("상세주소"),
    JOB("직업"),
    FAMILY_PHONE("신청자 외의 보호자가 있다면 보호자 연락처를 작성해주세요"),
    FAMILY("연락처를 작성한 보호자와의 관계를 작성해주세요"),
    EXPERIENCE("반려동물을 양육한 경험이 있나요?"),
    APPLICATION_REASON("입양을 신청하는 이유를 작성해주세요"),
    ADULT_COUNT("동거인 어른 수 (본인 포함)"),
    CHILDREN_COUNT("동거인 자녀 수"),
    ALL_CONSENT("동거인 모두 입양에 동의하나요?"),
    HOUSING_TYPE("주거 형태를 선택해주세요."),
    HAS_ALLERGY("동거인 중 알레르기 있는 사람이 있나요?"),
    HAS_OTHER_PETS("기존에 키우고 있는 반려동물이 있나요?"),
    CONSENT_FOR_CHECK("입양 후 상태 확인 등에 동의하시나요?");

    private final String question;


}