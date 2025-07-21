package com.project.nyang.global.security.oauth2;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    //로그인 동작을 커스텀으로 구현하고 싶을 때 사용하는 인터페이스

    //OAuth2 로그인 성공시 호출되는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {


        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String snsAccessToken = (String) attributes.get("snsAccessToken");
        String accessToken = (String) attributes.get("accessToken");
        String refreshToken = (String) attributes.get("refreshToken");
        String name = (String) attributes.get("name");

        System.out.println("[OAuth2_LOG]" + "소셜 로그인 시도한 이름 = "+name);

        // 사용자 ID를 안전하게 꺼내기 (null 체크 및 타입 캐스팅)
        Long id = null;
        Object idObj = attributes.get("id");
        if (idObj != null) {
            // Long 타입이 아닐 수도 있으니 안전하게 변환
            id = Long.valueOf(idObj.toString());
        }


        //토큰 전달방식
        // 또는, 보안을 강화하려면 아래처럼 HttpOnly 쿠키로 전달해도 됨
        // accessToken
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false) // ✅ 로컬에서는 false
                .sameSite("Lax") // ✅ Lax 또는 Strict로 변경
                .path("/")
                .build();
        response.addHeader("Set-Cookie", accessTokenCookie.toString());

// refreshToken
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // ✅ 로컬에서는 false
                .sameSite("Lax") // ✅
                .path("/")
                .build();
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        /** SNS AccessToken */
        ResponseCookie snsAccessTokenCookie = ResponseCookie.from("sns_access_token", snsAccessToken)
                .httpOnly(false)         // JS에서 읽을 수 있게 유지
                .secure(false)           // ✅ 로컬에서는 false
                .sameSite("Lax")         // ✅ 'None' 대신 'Lax' 사용 (이러면 secure 요구 안 함)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", snsAccessTokenCookie.toString());

        response.sendRedirect("http://localhost:8081/oauth2/redirect?token=" + accessToken);

    }
}