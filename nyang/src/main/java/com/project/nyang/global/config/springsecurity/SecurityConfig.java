package com.project.nyang.global.config.springsecurity;

import com.project.nyang.global.security.jwt.JwtTokenFilter;
import com.project.nyang.global.security.oauth2.OAuth2LoginSuccessHandler;
import com.project.nyang.global.security.oauth2.OAuth2LogoutSuccessHandler;
import com.project.nyang.global.security.oauth2.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //설정 클래스 등록
@EnableWebSecurity //스프링 시큐리티 활성화
@RequiredArgsConstructor //생성자 자동 생성
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    private final OAuth2LogoutSuccessHandler oAuth2LogoutSuccessHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2UserService oAuth2UserService;


    //스프링 시큐리티에서 어떤 순서로 어떤 보안 규칙의 필터를 거칠지 정의하는 클래스
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                //CSRF 보호 기능 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth)->auth
                                 //인증 필요없음(정적 리소스 (HTML, JS, CSS, 이미지 등))
                                .requestMatchers(
                                        "/", "/index.html"
                                ).permitAll()

                                // ✅ Swagger UI 허용 경로 추가
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/v3/api-docs.yaml",
                                        "/swagger-resources/**"
                                ).permitAll()

                                //인증필요없음
                                .requestMatchers(
                                        "/api/auth/**",       // 로그인/리프레시/로그아웃 등 인증 없이 사용
                                        "/oauth2/**",         // OAuth2 리디렉션
                                        "/login/**",          // 스프링 시큐리티 내부 로그인 관련
                                        "/api/test/**", // ✅ 이 줄 추가: 공통 응답 테스트 컨트롤러 허용
                                        "/*"
                                ).permitAll()

                                .requestMatchers(
                                        "/api/user/**" // 사용자 정보 관련 API
                                ).authenticated() //인증이 필요한 경로


                )

                //스프링 시큐리티에서 세션관리정책을 설정하는 부분
                //기본적으로 스프링시큐리티는 세션을 생성함
                //하지만 JWT 기반 인증은 세션상태를 저장하지 않는 무상태방식
                //인증 정보를 세션에 저장하지 않고, 매 요청마다 토큰으로 인증
                .sessionManagement(session ->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //매 요청마다 적용할 필터
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)



                .oauth2Login(oauth2->oauth2
//                        .loginPage("/index.html")
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                )

                .logout(logout -> logout
                        .logoutUrl("/logout") /* 저희 로그아웃 이쪽경로로 날려주세요*/
                        .logoutSuccessHandler(oAuth2LogoutSuccessHandler)
                        .permitAll()
                )


                .build(); //위 명시한 설정들을 적용
    }

}