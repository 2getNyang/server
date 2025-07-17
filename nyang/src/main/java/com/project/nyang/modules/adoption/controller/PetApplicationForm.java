package com.project.nyang.modules.adoption.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PetApplicationForm 입양신청에 대한 기능들을 작성합니다
 *
 * @author : 이지은
 * @fileName : PetApplicationForm
 * @since : 25. 7. 14.
 */
@Tag(name="Adoption API", description = "입양신청에 대한 API 입니다.")
@RestController
@RequestMapping("/api/v1/adoption")
public class PetApplicationForm {


}