package com.project.nyang.global.security.oauth2;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2LogoutSuccessHandler implements LogoutSuccessHandler {

    // 로그아웃을 커스텀으로 구현하고싶을때 사용하는 인터페이스

    // 카카오 REST API 키 (환경변수나 properties에서 가져오세요)
            private final String kakaoClientId = "${KAKAO_ID}";
    private final String kakaoLogoutRedirectUri = "http://localhost:8080"; // 앱 환경에 맞게 변경
    private final String  naverLogoutRedirectUri = "http://localhost:8080/login.html";
    private final String naverLogoutUrl = "https://nid.naver.com/nidlogin.logout?returl=" + naverLogoutRedirectUri;



    // 로그아웃 성공시 호출되는 메서드
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

//TODO: 나중에 프론트랑 연동시 수정해야함.
        // 기본 리디렉션 URL → 일반 로그아웃 시 index.html로 이동
        String redirectUrl = "/";

        if (authentication!=null && authentication.getPrincipal() instanceof DefaultOAuth2User auth2User){

            Map<String,Object> attributes = auth2User.getAttributes();
            System.out.println("attributes: "+attributes);

            Object email = attributes.get("email");
            // 네이버 로그아웃
            if (email!=null && email.toString().endsWith("@naver.com")){

                System.out.println("네이버 로그아웃입니다.");

                redirectUrl = "https://nid.naver.com/nidlogin.logout?returl=" + naverLogoutUrl;
            }

            // 카카오 로그인 사용자인 경우 (attributes에 'id' 키가 있음)
            else if (attributes.containsKey("id")) {
                System.out.println("카카오 로그아웃입니다.");

                redirectUrl = "https://kauth.kakao.com/oauth/logout?client_id=" + kakaoClientId
                        + "&logout_redirect_uri=" + kakaoLogoutRedirectUri;
            }

        }
        /* 냅다 쿠키삭제  = 소셜로그인은 쿠키삭제래요 */

        deleteCookie(response, "accessToken");
        deleteCookie(response, "refreshToken");

        // 최종적으로 redirectUrl로 리디렉트
        response.sendRedirect(redirectUrl);
    }

/* 쿠키 삭제 메서드 */
    private void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
