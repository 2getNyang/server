package com.project.nyang.modules.board.lost.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.modules.board.lost.dto.*;
import com.project.nyang.modules.board.lost.service.LostService;
import com.project.nyang.reference.dto.KindDTO;
import com.project.nyang.reference.dto.SubRegionDTO;
import com.project.nyang.reference.repository.KindRepository;
import com.project.nyang.reference.repository.SubRegionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 실종/목격 게시판 컨트롤러입니다.
 *
 * @author : 선순주
 * @fileName : LostController
 * @since : 2025-07-09
 */
@Tag(name = "\uD83D\uDEA8 실종/목격 게시판 API", description = "실종/목격 게시판 API 문서입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/lost")
@Slf4j
public class LostController {

    private final LostService lostService;
    private final KindRepository kindRepository;
    private final SubRegionRepository subRegionRepository;
    private final Long categoryId =  4L;

    @Operation(summary = "실종/목격 게시글 전체 조회", description = """
            실종/목격 게시판의 게시글을 전체 조회합니다. 
            
            기본적으로 한페이지에 12개의 글을 조회하며, page는 0부터 시작합니다.
            """)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<LostListResponseDTO>>> getBoardsByCategory(
            @ParameterObject @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<LostListResponseDTO> boards = lostService.getLostBoard(categoryId, pageable);
        return ResponseEntity.ok(ApiSuccessResponse.success(boards));
    }

    //게시글 단일 조회
    @Operation(summary = "실종/목격 게시글 단일 조회", description = """
            실종/목격 게시판의 특정 게시글을 단일 조회 합니다. boardId에 해당 하는 글을 반환합니다.
            """)
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<LostDetailResponseDTO>> getBoardDetail(
            @Parameter(description = "조회할 게시글의 ID", example = "13")
            @PathVariable Long boardId)
    {
        log.info("✅실종/목격 게시판 단일 조회");
        LostDetailResponseDTO responseDTO = lostService.getLostDetail(boardId);
        return ResponseEntity.ok(ApiSuccessResponse.success(responseDTO));
    }

    //게시글 저장폼 관련
    @Operation(summary = "작성 폼 select 데이터 조회")
    @GetMapping("/form-info")
    public ResponseEntity<ApiResponse<LostCreateFormDTO>> getFormInfo() {
        LostCreateFormDTO formInfo = lostService.getLostFormInfo();
        return ResponseEntity.ok(ApiSuccessResponse.success(formInfo));
    }

    //축종 조회
    @Operation(summary = "축종별 품종 조회")
    @GetMapping("/upkind/{upKindCd}/kinds")
    public ResponseEntity<List<KindDTO>> getKindsByUpKind(@PathVariable String upKindCd) {
        List<KindDTO> kinds = kindRepository.findByUpKindCd_UpKindCd(upKindCd).stream()
                .map(KindDTO::from)
                .toList();
        return ResponseEntity.ok(kinds);
    }

    //시도
    @Operation(summary = "시도별 시군구 조회")
    @GetMapping("/regions/{regionCode}/sub-regions")
    public ResponseEntity<List<SubRegionDTO>> getSubRegions(@PathVariable String regionCode) {
        List<SubRegionDTO> subRegions = subRegionRepository.findByRegion_RegionCode(regionCode).stream()
                .map(SubRegionDTO::from)
                .toList();
        return ResponseEntity.ok(subRegions);
    }



    //게시글 저장
    @Operation(summary = "실종/목격 게시글 저장", description = """
            실종/목격 게시판에 글을 저장합니다. 인증된 사용자여야하며, 최대 5개까지 이미지 첨부가 가능합니다.
            """)
    @PostMapping (consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<Long>> createLostBoard(
            @RequestPart("dto") LostCreateRequestDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        Long boardId = lostService.createLostBoard(dto, images);
        return ResponseEntity.ok(ApiSuccessResponse.success(boardId));
    }

    //물리적 게시글 삭제
//    @DeleteMapping("/{boardId}")
//    public ResponseEntity<ApiResponse<Long>> deleteBoard(@PathVariable Long boardId){
//        lostService.deleteLostBoard(boardId);
//        return ResponseEntity.ok(ApiSuccessResponse.success(boardId, "게시글이 삭제되었습니다."));
//    }

    //소프트 딜리트 : 게시글 삭제
    @Operation(summary = "실종/목격 게시글 삭제", description = """
            실종/목격 게시판에 글을 삭제합니다. 해당 게시글의 작성자와 로그인 된 사용자가 일치해야합니다.
            
            삭제는 soft Delete로 진행되며, 게시글 삭제 후 해당 글과 연관된 image와 s3 이미지 경로는 스케줄러를 통해
            매일 새벽 1시에 삭제된 지 1일이 경과하면 물리적 삭제가 진행됩니다.
            """)
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Long>> softDeleteBoard(
            @Parameter(description = "삭제할 게시글의 ID", example = "13")
            @PathVariable Long boardId)
    {
        lostService.softDeleteLostBoard(boardId);
        return ResponseEntity.ok(ApiSuccessResponse.success(boardId, "게시글이 삭제되었습니다."));
    }

    //게시글 수정폼 호출
    @Operation(summary = "실종/목격 게시글 수정폼 호출", description = """
            실종/목격 게시판의 게시글 수정폼 호출 API입니다. 글을 작성한 작성자와 로그인한 사용자가 일치해야하며, 해당 글의 정보들을 번환합니다.
            """)
    @GetMapping("/{boardId}/form")
    public ResponseEntity<ApiResponse<LostUpdateResponseDTO>> getBoardForEdit(
            @Parameter(description = "수정할 게시글의 ID", example = "13")
            @PathVariable Long boardId)
    {
        LostUpdateResponseDTO responseDTO = lostService.getBoardUpdateForm(boardId);
        return ResponseEntity.ok(ApiSuccessResponse.success(responseDTO,"수정 폼 호출 완료"));
    }

    //게시글 수정
    @Operation(summary = "실종/목격 게시글 수정", description = """
           실종/목격 게시판의 게시글 수정 API입니다. 사용자는 필드 뿐만 아니라 이미지도 삭제하거나 추가할 수 있습니다.
            """)
    @PutMapping(
            value = "/{boardId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<ApiResponse<?>> updateBoard(
            @Parameter(description = "수정할 게시글의 ID", example = "13")
            @PathVariable Long boardId,
            @RequestPart LostUpdateRequestDTO requestDTO,
            @RequestPart(required = false) List<MultipartFile> newImages
    ) {
        log.info("boardId(path) = {}", boardId);
        log.info("dto.boardId(body) = {}", requestDTO.getBoardId());
        lostService.updateBoard(boardId, requestDTO, newImages);
        return ResponseEntity.ok(ApiSuccessResponse.success(boardId,"게시글 수정 완료"));
    }
}
