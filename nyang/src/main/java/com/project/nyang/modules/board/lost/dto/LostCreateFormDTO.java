package com.project.nyang.modules.board.lost.dto;

import com.project.nyang.reference.dto.RegionDTO;
import com.project.nyang.reference.dto.UpKindDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * 실종/목격 제보게시판 글 작성 시, 사용자에게 보여줄 작성 폼 DTO입니다
 *
 * @author : 선순주
 * @fileName : LostCreateFormDTO
 * @since : 2025-07-17
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "실종/목격 게시글 작성 폼 정보 DTO")
public class LostCreateFormDTO {

    @Schema(description = "축종 목록 (ex. 개, 고양이 등)")
    private List<UpKindDTO> upKinds;    // 축종

    @Schema(description = "시/도 목록 (ex. 서울특별시, 경기도 등)")
    private List<RegionDTO> regions;    // 시도

    @Schema(
            description = "성별 선택값 목록 (M: 수컷, F: 암컷, Q: 미확인)",
            example = "[\"M\", \"F\", \"Q\"]"
    )
    private List<String> genders;       // ["M", "F", "Q"]

    @Schema(
            description = "제보 유형 선택값 (MS: 실종, WT: 목격)",
            example = "[\"MS\", \"WT\"]"
    )
    private List<String> lostTypes;     // ["MS", "WT"]

    @Schema(
            description = "추정 나이 선택값 (1~20세)",
            example = "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]"
    )
    private List<Integer> ages;         // [1 ~ 20]
}
