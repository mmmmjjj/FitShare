package com.fitshare.backend.api.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
public class RedisServiceImpl implements RedisService {
    private final static long EXPIRE_TIME = 3600L * 24 * 30; // 30일

    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private SetOperations<String, String> setOperations;

    private RedisServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.setOperations = redisTemplate.opsForSet();
    }

    // 키-벨류 설정
    public void setData(String token, String id) {
        valueOperations.set(token, id, Duration.ofSeconds(EXPIRE_TIME));
    }

    // 키값으로 벨류 가져오기
    public String getData(String token) {
        return valueOperations.get(token);
    }

    public void setDataExpire(String token, String id, long duration) {
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(token, id, expireDuration);
    }

    // 키-벨류 삭제
    public void delData(String token) {
        redisTemplate.delete(token);
    }


    // 만료 기간 가져오기
    public Long getExpire(String token) {
        return redisTemplate.getExpire(token);
    }


    // 세션에 입장 유저 id 저장
    @Override
    public void setSessionParticipant(String sessionId, String memberId) {
        setOperations.add(sessionId, memberId);
    }

    // 세션에 참여하고 있는 유저 id 가져오기
    @Override
    public Set<String> getSessionParticipants(String sessionId) {
        return setOperations.members(sessionId);
    }

    // 세션에서 나간 유저 id 삭제
    @Override
    public void delSessionParticipant(String sessionId, String memberId) {
        setOperations.remove(sessionId, memberId);
    }

    // 세션에 참여하고 있는 유저 수
    @Override
    public Long getSessionParticipantCount(String sessionId) {
        return setOperations.size(sessionId);
    }
}
