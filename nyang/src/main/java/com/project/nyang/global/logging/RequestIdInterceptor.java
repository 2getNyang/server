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

        // 사용자 ID도 설정 (인증 정보에서 가져오기)
        String userId = request.getHeader("X-User-ID");
        if (userId != null && !userId.isEmpty()) {
            RequestContext.setUserId(userId);
        } else {
            RequestContext.setUserId("anonymous");  // 비로그인 사용자
        }

        // 응답 헤더에도 추가
        response.setHeader("X-Request-ID", requestId);

        log.info("[{}] 요청 시작 - {} {} (사용자: {})",
                requestId, request.getMethod(), request.getRequestURI(),
                RequestContext.getUserId());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        String requestId = RequestContext.getRequestId();
        log.info("[{}] 요청 완료 - 상태코드: {}", requestId, response.getStatus());

        // ThreadLocal 정리 (메모리 누수 방지)
        RequestContext.clear();
    }
}
