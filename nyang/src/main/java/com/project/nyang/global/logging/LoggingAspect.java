package com.project.nyang.global.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author : 선순주
 * @packageName : com.project.nyang.global.logging
 * @fileName : LoggingAspect
 * @date : 2025-10-20
 * @description : Logging AOP
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // @LogMessage 어노테이션이 있는 메서드 로깅
    @Around("@annotation(com.project.nyang.global.logging.LogMessage)")
    public Object logWithCustomMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        String reqId = RequestContext.getRequestId();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 커스텀 어노테이션 가져오기
        LogMessage logMessage = method.getAnnotation(LogMessage.class);
        String customMessage = logMessage.value();
        String operation = logMessage.operation();
        Object[] args = joinPoint.getArgs();

        // 파라미터 이름과 값 함께 포맷팅
        String params = formatParametersWithNames(method, args);

        // 시작 로그
        if (!operation.isEmpty()) {
            log.info("[{}] [{}] {} 시작 - 파라미터: {}",
                    reqId, operation, customMessage, params);
        } else {
            log.info("[{}] {} 시작 - 파라미터: {}",
                    reqId, customMessage, params);
        }

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            // 결과 포맷팅
            String resultInfo = formatResult(result);

            // 완료 로그
            if (!operation.isEmpty()) {
                log.info("[{}] [{}] {} 완료 - {}, 소요시간: {}ms",
                        reqId, operation, customMessage, resultInfo, duration);
            } else {
                log.info("[{}] {} 완료 - {}, 소요시간: {}ms",
                        reqId, customMessage, resultInfo, duration);
            }

            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;

            if (!operation.isEmpty()) {
                log.error("[{}] [{}] {} 실패 - 소요시간: {}ms, 에러: {}",
                        reqId, operation, customMessage, duration, e.getMessage(), e);
            } else {
                log.error("[{}] {} 실패 - 소요시간: {}ms, 에러: {}",
                        reqId, customMessage, duration, e.getMessage(), e);
            }

            throw e;
        }
    }

    // 어노테이션 없는 일반 Service 메서드도 처리
    @Around("within(@org.springframework.stereotype.Service *) " +
            "&& !@annotation(com.project.nyang.global.logging.LogMessage) " +
            "&& !@annotation(com.project.nyang.global.logging.NoLogging)")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        String reqId = RequestContext.getRequestId();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getMethod().getName();

        log.info("[{}] [SERVICE] {}.{} 시작", reqId, className, methodName);

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            String resultInfo = formatResult(result);
            log.info("[{}] [SERVICE] {}.{} 완료 - {}, 소요시간: {}ms",
                    reqId, className, methodName, resultInfo, duration);

            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("[{}] [SERVICE] {}.{} 실패 - 소요시간: {}ms, 에러: {}",
                    reqId, className, methodName, duration, e.getMessage(), e);
            throw e;
        }
    }

    private String formatParameters(Object[] args) {
        if (args == null || args.length == 0) {
            return "없음";
        }

        return Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) return "null";
                    if (arg instanceof String || arg instanceof Number || arg instanceof Boolean) {
                        return arg.toString();
                    }
                    return arg.getClass().getSimpleName();
                })
                .collect(Collectors.joining(", "));
    }

    private String formatResult(Object result) {
        if (result == null) return "결과: null";

        if (result instanceof Page) {
            Page<?> page = (Page<?>) result;
            return String.format("조회: 전체 %d건, 현재 %d건",
                    page.getTotalElements(), page.getNumberOfElements());
        }

        if (result instanceof java.util.List) {
            return String.format("조회: %d건", ((java.util.List<?>) result).size());
        }

        String className = result.getClass().getSimpleName();
        if (className.contains("Response") || className.contains("DTO")) {
            return "반환: " + className;
        }

        return "처리완료";
    }

    // 파라미터 이름과 값을 함께 표시
    private String formatParametersWithNames(Method method, Object[] args) {
        if (args == null || args.length == 0) {
            return "없음";
        }

        java.lang.reflect.Parameter[] parameters = method.getParameters();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");

            String paramName = parameters[i].getName();
            Object arg = args[i];

            sb.append(paramName).append("=");

            if (arg == null) {
                sb.append("null");
            } else if (arg instanceof String) {
                // String은 길이 제한 (너무 길면 잘라내기)
                String str = (String) arg;
                if (str.length() > 50) {
                    sb.append("\"").append(str.substring(0, 47)).append("...\"");
                } else {
                    sb.append("\"").append(str).append("\"");
                }
            } else if (arg instanceof Number || arg instanceof Boolean) {
                sb.append(arg);
            } else if (arg instanceof java.util.Collection) {
                // List, Set 등은 개수만 표시
                sb.append(arg.getClass().getSimpleName())
                        .append("(").append(((java.util.Collection<?>) arg).size()).append("건)");
            } else {
                // 객체 타입은 클래스명만 표시
                sb.append(arg.getClass().getSimpleName());
            }
        }

        return sb.toString();
    }
}