package com.project.nyang.global.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : 선순주
 * @packageName : com.project.nyang.global.logging
 * @fileName : LogMessage
 * @date : 2025-10-20
 * @description : 로그 커스텀 어노테이션
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMessage {
    String value();  // 로그에 표시할 메시지
    String operation() default "";  // CREATE, READ, UPDATE, DELETE 등
}

