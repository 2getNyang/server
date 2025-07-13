package com.project.nyang.global.security.jwt;

import com.project.nyang.global.security.core.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *
 * JwtTokenFilter 클래스입니다.
 * @fileName        : JwtTokenFilter
 * @author          : 오승훈
 * @since           : 2025-07-08
 *
 */

/**
 *  JwtTokenFilter
 *
 * - 매 요청마다 JWT 토큰을 확인하고
 * - 유효한 경우 인증 정보를 SecurityContext 에 등록하는 필터
 * - Spring Security의 OncePerRequestFilter를 상속하여 요청당 한 번만 실행됨
 */
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    //JwtTokenFilter 모든 HTTP요청을 가로채서 JWT 토큰을 검사하는 필터 역할
    //OncePerRequestFilter는 한 요청당 딱 한번만 실행되는 필터 역할

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     *  실제 필터 로직이 실행되는 메서드
     * - 요청에서 JWT 토큰을 추출
     * - 유효성 검사
     * - 인증 객체 생성 후 SecurityContext 에 저장
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = getTokenFromRequest(request);

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            //토큰에서 사용자를 꺼내서 담은 사용자 인증 객체
            UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(accessToken);

            //http 요청으로부터 부가 정보(ip, 세션 등)를 추출해서 사용자 인증 객체에 넣어줌
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //토큰에서 사용자 인증정보를 조회해서 인증정보를 현재 스레드에 인증된 사용자로 등록
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // JwtTokenFilter을 거치고 다음 필터로 넘어감
        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 Bearer 토큰 추출
     *
     * @param request 현재 HTTP 요청
     * @return JWT 토큰 문자열 (없으면 null)
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후 토큰 값만 추출
        }
        return null;
    }

    /**
     * JWT 토큰을 기반으로 인증 객체 생성
     *
     * @param token 클라이언트로부터 전달받은 JWT 토큰
     * @return 인증된 UsernamePasswordAuthenticationToken 객체
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {

        //JWT 토큰에서 사용자 id 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        //위 추출한 id를 DB에서 사용자 정보 조회
        UserDetails userDetails = customUserDetailsService.loadUserById(userId);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}
