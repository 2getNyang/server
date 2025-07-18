package com.project.nyang.modules.board.lost.dto;

import com.project.nyang.reference.dto.RegionDTO;
import com.project.nyang.reference.dto.UpKindDTO;
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
public class LostCreateFormDTO {
    private List<UpKindDTO> upKinds;    // 축종
    private List<RegionDTO> regions;    // 시도
    private List<String> genders;       // ["M", "F", "Q"]
    private List<String> lostTypes;     // ["MS", "WT"]
    private List<Integer> ages;         // [1 ~ 20]
}
