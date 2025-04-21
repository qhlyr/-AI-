package com.qq.service.impl;

import com.qq.model.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatHistoryService {

    private static final String PREFIX = "chat:session:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveMessage(String sessionId, ChatMessage message) {
        String key = PREFIX + sessionId;
        redisTemplate.opsForList().rightPush(key, message);
    }

    public List<Object> getMessages(String sessionId) {
        String key = PREFIX + sessionId;
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
