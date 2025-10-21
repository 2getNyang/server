package com.project.nyang.global.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
/**
 * @author : 선순주
 * @packageName : com.project.nyang.global.logging
 * @fileName : RequestIdInterceptor
 * @date : 2025-10-19
 * @description : 인터셉터를 이용하여 요청마다 ID 생성
 */
@Slf4j
@Component
public class RequestIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        // 요청 ID 생성 또는 헤더에서 가져오기
        String requestId = request.getHeader("X-Request-ID");
        if (requestId == null || requestId.isEmpty()) {
            requestId = RequestContext.generateRequestId();
        }

        RequestContext.setRequestId(requestId);

        // Spring Security에서 인증된 사용자 정보 가져오기
        String userId = getUserIdFromSecurity();
        RequestContext.setUserId(userId);

        response.setHeader("X-Request-ID", requestId);

        log.info("[{}] 요청 시작 - {} {} (사용자 ID: {})",
                requestId, request.getMethod(), request.getRequestURI(),
                RequestContext.getUserId());

        return true;
    }

    /**
     * Spring Security에서 현재 인증된 사용자 ID를 가져옵니다.
     */
    private String getUserIdFromSecurity() {
        try {
            org.springframework.security.core.Authentication authentication =
                    org.springframework.security.core.context.SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();

                // CustomUserDetails인 경우
                if (principal instanceof com.project.nyang.global.security.core.CustomUserDetails) {
                    com.project.nyang.global.security.core.CustomUserDetails userDetails =
                            (com.project.nyang.global.security.core.CustomUserDetails) principal;
                    return String.valueOf(userDetails.getId());
                }

                // String (username)인 경우
                if (principal instanceof String && !"anonymousUser".equals(principal)) {
                    return (String) principal;
                }
            }
        } catch (Exception e) {
            log.debug("사용자 인증 정보를 가져오는데 실패했습니다: {}", e.getMessage());
        }

        return "anonymous";
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        String requestId = RequestContext.getRequestId();
        log.info("[{}] 요청 완료 - 상태코드: {}", requestId, response.getStatus());

        RequestContext.clear();
    }
}

