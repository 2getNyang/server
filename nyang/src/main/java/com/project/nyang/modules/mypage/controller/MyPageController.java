package com.project.nyang.modules.mypage.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.mypage.dto.*;
import com.project.nyang.modules.mypage.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 마이페이지 controller
 *
 * @author : 박세정
 * @fileName : MyPageController
 * @since : 2025-07-12
 */
@Tag(name = "Mypage API", description = "마이페이지 Controller")
@RestController
@RequestMapping("/api/v1/my")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private static final Long REVIEW_CATEGORY_ID = 2L;
    private static final Long SNS_CATEGORY_ID = 3L;
    private static final Long LOST_CATEGORY_ID = 4L;

    @Operation(summary = "찜한 입양공고 리스트 조회")
    @GetMapping("/bookmarks")
    public ResponseEntity<ApiResponse<Page<MyAnimalDTO>>> getMyAnimals(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(ApiSuccessResponse.success(myPageService.getMyAnimals(userId, page, size), "찜한 입양 공고 리스트 조회 성공"));
    }

    @Operation(summary = "좋아요한 게시글 리스트 조회")
    @GetMapping("/likes")
    public ResponseEntity<ApiResponse<Page<MyLikedBoardDTO>>> getLikedBoards(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(ApiSuccessResponse.success(myPageService.getLikedBoards(userId, page, size), "좋아요한 게시글 리스트 조회 성공"));
    }

    @Operation(summary = "사용자가 작성한 입양 후기 게시글 리스트 조회")
    @GetMapping("/boards/reveiw")
    public ResponseEntity<ApiResponse<Page<MyBoardDTO>>> getMyReveiwBoards(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok((ApiSuccessResponse.success(myPageService.getMyBoards(userId, REVIEW_CATEGORY_ID, page, size), "작성한 입양 후기 게시글 리스트 조회 성공")));
    }  
    
    @Operation(summary = "사용자가 작성한 sns 홍보 게시글 리스트 조회")
    @GetMapping("/boards/sns")
    public ResponseEntity<ApiResponse<Page<MyBoardDTO>>> getMySnsBoards(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok((ApiSuccessResponse.success(myPageService.getMyBoards(userId, SNS_CATEGORY_ID, page, size), "작성한 sns 홍보 게시글 리스트 조회 성공")));
    }

    @Operation(summary = "사용자가 작성한 실종/목격 게시글 리스트 조회")
    @GetMapping("/boards/lost")
    public ResponseEntity<ApiResponse<Page<MyLostBoardDTO>>> getMyLostBoards(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok((ApiSuccessResponse.success(myPageService.getMyLostBoards(userId, LOST_CATEGORY_ID, page, size), "작성한 sns 홍보 게시글 리스트 조회 성공")));
    }

    @Operation(summary = "사용자의 입양 신청 목록 조회")
    @GetMapping("/adoption")
    public ResponseEntity<ApiResponse<Page<MyPetApplicationFormDTO>>> getPetApplicationForms(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(ApiSuccessResponse.success(myPageService.getPetApplicationForms(userId, page, size), "입양 신청 내역 조회 성공"));
    }


}