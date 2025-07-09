package com.project.nyang.modules.auth.controller;

import com.project.nyang.modules.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    /** 토큰갱신 API **/
    //refresh HTTP 요청 헤더에서 토큰을 추출하고 그 토큰으로 리프레시 토큰을 발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader(value = "Authorization", required = false)
                                          String authorizationHeader, HttpServletRequest request) {
        String refreshToken = null;
        //1. 쿠키에서 찾기
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        //2. Authorization 헤더 찾기
        if(refreshToken == null&& authorizationHeader != null&& authorizationHeader.startsWith("Bearer ")) {
            refreshToken = authorizationHeader.replace("Bearer ", "").trim();
        }
        if(refreshToken != null&&refreshToken.isEmpty()) {
            throw new IllegalArgumentException("리프레시 토큰이 없습니다.");
        }
        String newAccessToken = authService.refreshToken(refreshToken);
        //json 객체로 변환하여 front에 내려주기
        Map<String, String> res = new HashMap<>();
        res.put("accessToken", newAccessToken);
        res.put("refreshToken", refreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }



    //소셜로그인은 브라우저에 쿠키가 저장되는데 그걸 삭제 해야함
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        // accessToken 쿠키 삭제
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0); // 즉시 만료!

        // refreshToken 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        // 응답에 쿠키 삭제 포함
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // (추가) 서버 세션도 있다면 만료
        // request.getSession().invalidate();

        return ResponseEntity.ok().body("로그아웃 완료 (쿠키 삭제됨)");
    }
}
