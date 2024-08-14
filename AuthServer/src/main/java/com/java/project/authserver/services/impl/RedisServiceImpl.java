package com.java.project.authserver.services.impl;

import com.java.project.authserver.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getValue(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }
}
