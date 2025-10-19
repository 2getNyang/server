package com.project.nyang.global.logging;

/**
 * @author : 선순주
 * @packageName : com.project.nyang.global.logging
 * @fileName : RequestContext
 * @date : 2025-10-19
 * @description : ThreadLocal로 요청 추적 ID관리
 */
import java.util.UUID;

public class RequestContext {

    private static final ThreadLocal<String> requestIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> userIdHolder = new ThreadLocal<>();

    public static void setRequestId(String requestId) {
        requestIdHolder.set(requestId);
    }

    public static String getRequestId() {
        return requestIdHolder.get();
    }

    public static void setUserId(String userId) {
        userIdHolder.set(userId);
    }

    public static String getUserId() {
        return userIdHolder.get();
    }

    public static String generateRequestId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static void clear() {
        requestIdHolder.remove();
        userIdHolder.remove();
    }
}
