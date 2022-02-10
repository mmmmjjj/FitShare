package com.fitshare.backend.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final static long EXPIRE_TIME = 3600L * 24 * 30; // 30일

    private final RedisTemplate<String, String> redisTemplate;

    // 키-벨류 설정
    public void setData(String token, String id) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, id, Duration.ofSeconds(EXPIRE_TIME));
    }

    // 키값으로 벨류 가져오기
    public String getData(String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(token);
    }

    public void setDataExpire(String token, String id, long duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        values.set(token, id, expireDuration);
    }

    // 키-벨류 삭제
    public void delData(String token) {
        redisTemplate.delete(token);
    }


    // 만료 기간 가져오기
    public Long getExpire(String token) {
        return redisTemplate.getExpire(token);
    }
}
