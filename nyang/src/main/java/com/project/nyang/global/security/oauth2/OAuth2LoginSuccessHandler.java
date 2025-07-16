package com.project.nyang.global.security.oauth2;


import com.project.nyang.global.security.jwt.JwtTokenProvider;
import com.project.nyang.modules.auth.entity.Auth;
import com.project.nyang.modules.auth.repository.AuthRepository;
import com.project.nyang.modules.user.dto.UserInfoDTO;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    public final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    //로그인 동작을 커스텀으로 구현하고 싶을 때 사용하는 인터페이스

    //OAuth2 로그인 성공시 호출되는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {


        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauthToken.getAuthorizedClientRegistrationId(); // "naver", "kakao", "google"
        String principalName = oauthToken.getName();

        // ✅ 소셜 access / refresh token
        OAuth2AuthorizedClient authorizedClient =
                authorizedClientService.loadAuthorizedClient(registrationId, principalName);

        String snsAccessToken = authorizedClient.getAccessToken().getTokenValue();
        String snsRefreshToken = authorizedClient.getRefreshToken() != null
                ? authorizedClient.getRefreshToken().getTokenValue()
                : null;

        // ✅ 사용자 정보 추출
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) oauthToken.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // ✅ login_id 생성
        String loginId;
        if ("naver".equals(registrationId)) {
            Map<String, Object> responseMap = (Map<String, Object>) attributes.get("response");
            loginId = responseMap.get("id").toString() + "@naver";
        } else if ("kakao".equals(registrationId)) {
            loginId = attributes.get("id").toString() + "@kakao";
        } else if ("google".equals(registrationId)) {
            loginId = attributes.get("sub").toString() + "@google";
        } else {
            throw new RuntimeException("지원하지 않는 소셜 로그인입니다: " + registrationId);
        }

        // ✅ user 조회
        UserInfoDTO user = userRepository.findByLoginTypeAndLoginId(registrationId, loginId)
                .orElseThrow();

        // ✅ JWT 발급
        String jwtAccessToken = jwtTokenProvider.createAccessToken(user);
        String jwtRefreshToken = jwtTokenProvider.createRefreshToken(user);

        // ✅ AUTH 저장
        Auth auth = Auth.builder()
                .user(user)
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .snsAccessToken(snsAccessToken)
                .snsRefreshToken(snsRefreshToken)
                .tokenType("Bearer")
                .snsId(loginId)
                .build();
        authRepository.save(auth);

        //토큰 전달방식
        // 또는, 보안을 강화하려면 아래처럼 HttpOnly 쿠키로 전달해도 됨
        Cookie accessTokenCookie = new Cookie("accessToken", jwtAccessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
      //  accessTokenCookie.setMaxAge(60 * 3); // 3분짜리 임시쿠키


        Cookie refreshTokenCookie = new Cookie("refreshToken", jwtRefreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
       // refreshTokenCookie.setMaxAge(60 * 60 * 24); // 1일짜리


    }
}
