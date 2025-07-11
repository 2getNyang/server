package com.project.nyang.modules.auth.controller;

import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.global.security.oauth2.OAuth2UnlinkService;
import com.project.nyang.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

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
    private final OAuth2UnlinkService oAuth2UnlinkService;
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
    @Operation(
            summary = "네이버 연동 해제 (토큰 폐기)",
            description = """
                    네이버 소셜 로그인 연동 해제(토큰 폐기)를 수행합니다.  
                    연동 해제 후 사용자는 동일 네이버 계정으로 재로그인 시 신규 회원으로 가입 처리됩니다.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "연동 해제(토큰 폐기) 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @PostMapping("/unlink")
    public ResponseEntity<?> unlinkNaver(
            @RequestParam String accessToken
    ){
        oAuth2UnlinkService.unlinkNaver(accessToken);
        return ResponseEntity.ok().build();
    }

    //    //소셜로그인은 브라우저에 쿠키가 저장되는데 그걸 삭제 해야함
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletResponse response) {
//        // accessToken 쿠키 삭제
//        Cookie accessTokenCookie = new Cookie("accessToken", null);
//        accessTokenCookie.setHttpOnly(true);
//        accessTokenCookie.setPath("/");
//        accessTokenCookie.setMaxAge(0); // 즉시 만료!
//        System.out.println("accessTokenCookie: " + accessTokenCookie);
//
//        // refreshToken 쿠키 삭제
//        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(0);
//
//        // 응답에 쿠키 삭제 포함
//        response.addCookie(accessTokenCookie);
//        response.addCookie(refreshTokenCookie);
//
//        // (추가) 서버 세션도 있다면 만료
//        // request.getSession().invalidate();
//
//        return ResponseEntity.ok().body("로그아웃 완료 (쿠키 삭제됨)");
//    }



}
