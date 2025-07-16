package com.project.nyang.modules.auth.controller;

import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * AuthController 클래스입니다.
 * @fileName        : AuthController
 * @author          : 오승훈
 * @since           : 2025-07-08
 *
 */

@Tag(name = "인증 API", description = "JWT 토큰 관련 API (재발급 등)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
//    private final OAuth2UnlinkService oAuth2UnlinkService;
    /**
     * 토큰갱신 API
     **/
    //refresh HTTP 요청 헤더에서 토큰을 추출하고 그 토큰으로 리프레시 토큰을 발급
    @Operation(
            summary = "🔁 토큰 재발급",
            description = "쿠키 또는 Authorization 헤더에 포함된 Refresh Token을 사용해 새로운 Access Token을 발급합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "새로운 토큰 발급 성공 (accessToken, refreshToken 반환)",
            content = @Content(mediaType = "application/json")
    )
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @Parameter(description = "Authorization 헤더의 Refresh Token", example = "Bearer {refreshToken}")
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            HttpServletRequest request) {
        String refreshToken = null;
        //1. 쿠키에서 찾기
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        //2. Authorization 헤더 찾기
        if (refreshToken == null && authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            refreshToken = authorizationHeader.replace("Bearer ", "").trim();
        }
        if (refreshToken != null && refreshToken.isEmpty()) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_MISSING);
        }
        String newAccessToken = authService.refreshToken(refreshToken);
        //json 객체로 변환하여 front에 내려주기
        Map<String, String> res = new HashMap<>();
        res.put("accessToken", newAccessToken);
        res.put("refreshToken", refreshToken);

        return ResponseEntity.ok(ApiSuccessResponse.success(res, "토큰이 성공적으로 갱신되었습니다."));
    }
//    @Operation(
//            summary = "네이버 연동 해제 (토큰 폐기)",
//            description = """
//                    네이버 소셜 로그인 연동 해제(토큰 폐기)를 수행합니다.
//                    연동 해제 후 사용자는 동일 네이버 계정으로 재로그인 시 신규 회원으로 가입 처리됩니다.
//                    """,
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "연동 해제(토큰 폐기) 성공"),
//                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
//                    @ApiResponse(responseCode = "500", description = "서버 오류")
//            }
//    )
//    @PostMapping("/unlink")
//    public ResponseEntity<?> unlinkNaver(
//            @RequestParam String accessToken
//    ){
//        oAuth2UnlinkService.unlinkNaver(accessToken);
//        return ResponseEntity.ok().build();
//    }


    @Operation(
            summary = "구글 연동 해제 (토큰 폐기)",
            description = """
                    구글 소셜 로그인 연동 해제(토큰 폐기)를 수행합니다.
                    연동 해제 후 사용자는 동일 구글 계정으로 재로그인 시 신규 회원으로 가입 처리됩니다.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "연동 해제(토큰 폐기) 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @PostMapping("/revoke")
    public ResponseEntity<?> revokeGoogle(@RequestHeader("Authorization") String authorizationHeader){
        //Todo. DB에서 회원삭제처리에 대한 timestamp 처리는 있던데 해당 계정의 상태 컬럼은 따로 없는 것 같다. 07-14 회의후 User테이블 수정해야할 것 같다.
        String accessToken = authorizationHeader.replace("Bearer ", "");
        boolean success = authService.revokeGoogleAccessToken(accessToken);

        if (success) {
            //Todo. 아직 회원 탈퇴 기능 작성 전이라 주석처리 해둠. 현재 계정연동끊는 기능만 하는중
            //userService.markUserWithdrawnByAccessToken(accessToken);
            return ResponseEntity.ok("구글 연결 해제 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("구글 연결 해제 실패");
        }

    }

}
