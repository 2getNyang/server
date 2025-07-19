package com.project.nyang.global.security.oauth2;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2LogoutSuccessHandler implements LogoutSuccessHandler {

    // 로그아웃을 커스텀으로 구현하고싶을때 사용하는 인터페이스

    // 로그아웃 성공시 호출되는 메서드
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

//TODO: 나중에 프론트랑 연동시 수정해야함.
        // 기본 리디렉션 URL → 일반 로그아웃 시 index.html로 이동
        String redirectUrl = "/";

        /* 냅다 쿠키삭제  = 소셜로그인은 쿠키삭제래요 */

        deleteCookie(response, "accessToken");
        deleteCookie(response, "refreshToken");
        deleteCookie(response, "sns_access_token");

        // 최종적으로 redirectUrl로 리디렉트
        response.sendRedirect(redirectUrl);
    }

/* 쿠키 삭제 메서드 */
    private void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .path("/")
                .maxAge(0)
                .httpOnly(true) // 필요에 따라 false
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

}
