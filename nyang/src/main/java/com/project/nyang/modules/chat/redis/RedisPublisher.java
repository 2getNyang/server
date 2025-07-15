package com.project.nyang.modules.chat.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author : 선순주
 * @fileName : RedisPublisher
 * @since : 2025-07-14
 */
@Component
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(String topic, String jsonMessage) {
        redisTemplate.convertAndSend(topic, jsonMessage);
    }
}
