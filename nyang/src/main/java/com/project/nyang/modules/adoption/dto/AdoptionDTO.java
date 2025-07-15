package com.project.nyang.modules.adoption.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 입양 신청서 저장에 사용하는 DTO
 *
 * @author : 이지은
 * @fileName : AdoptionDTO
 * @since : 25. 7. 14.
 */
@Getter
@Setter
public class AdoptionDTO {

    //입양신청서 Id, 식별자
    private Long id;

    //입양신청자 이름
    private String userName;

    //Todo.입양신청자 아이디 컬럼 추가 및 Entity에 추가, 관계 추가도!


}