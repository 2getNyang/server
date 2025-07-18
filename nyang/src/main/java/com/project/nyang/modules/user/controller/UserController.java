package com.project.nyang.modules.user.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.user.dto.ChatUserInfoDTO;
import com.project.nyang.modules.user.dto.AuthInfoDTO;
import com.project.nyang.modules.user.dto.UpdateUserInfoDTO;
import com.project.nyang.modules.user.dto.UserInfoDTO;
import com.project.nyang.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * UserController입니다
 *
 * @author : 엄아영
 * @fileName : UserController
 * @since : 2025-07-13
 */

@Tag(name = "사용자 API", description = "사용자 관련 API")
@SecurityRequirement(name = "jwt token")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //채팅방에서 상대방 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChatUserInfoDTO>> getChatUserInfo(@PathVariable Long id) {
        ChatUserInfoDTO dto = userService.getChatUserInfo(id);
        return ResponseEntity.ok(ApiSuccessResponse.success(dto, "사용자 정보 조회 성공"));
    }

    //프론트엔드에 보낼 사용자 조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthInfoDTO>> getAuthInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        AuthInfoDTO authInfoDTO = userService.getAuthInfo(userId);
        return ResponseEntity.ok(ApiSuccessResponse.success(authInfoDTO, "사용자 정보 조회에 성공하였습니다"));
    }


    //사용자 정보 조회
    @Operation(summary = "사용자 정보 조회", description = "로그인 사용자의 기본 정보(닉네임·이메일·소셜 타입 등)를 반환")
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoDTO>> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        UserInfoDTO userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(ApiSuccessResponse.success(userInfo, "사용자 정보 조회에 성공하였습니다"));
    }

    //사용자 정보 수정
    //닉네임과 이메일만
    @Operation(summary = "사용자 정보 수정", description = "닉네임과 이메일 수정")
    @PutMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoDTO>> updateMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestBody UpdateUserInfoDTO updateUserInfoDTO) {
        Long userId = userDetails.getId();
        UserInfoDTO userInfo = userService.updateUserInfo(userId, updateUserInfoDTO);
        return ResponseEntity.ok(ApiSuccessResponse.success(userInfo, "사용자 정보 수정에 성공하였습니다"));
    }
}