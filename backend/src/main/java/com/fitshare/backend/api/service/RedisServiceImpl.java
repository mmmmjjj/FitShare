package com.fitshare.backend.api.service;

import com.fitshare.backend.db.entity.PrivateChat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Map;

@Slf4j
@Service
public class RedisServiceImpl implements RedisService {
    private final static long EXPIRE_TIME = 3600L * 24 * 30; // 30일

    private RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;
    private SetOperations<String, Object> setOperations;
    private HashOperations<String, Object, Object> hashOperations;

    private RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.setOperations = redisTemplate.opsForSet();
        this.hashOperations = redisTemplate.opsForHash();
    }

    // 키-벨류 설정
    @Override
    public void setData(String key, Object value) {
        if (value instanceof String) {
            valueOperations.set(key, value, Duration.ofSeconds(EXPIRE_TIME));
        } else if (value instanceof Long) {
            setOperations.add(key, String.valueOf(value));
        } else if (value instanceof PrivateChat) {
            // key : chat_senderId_receiverId_yyyy-MM-dd
            PrivateChat privateChat = (PrivateChat) value;
            hashOperations.put(key, privateChat.getCreatedTime(), privateChat.getMessage());
            // 삭제 이벤트를 발생시키기 위한 shadow key set
            valueOperations.set("shadowkey:" + key, "", getSecondsUntilTomorrow());
        }
    }

    // 키값으로 벨류 가져오기
    @Override
    public Object getData(String key) {
        return valueOperations.get(key);
    }

    // 키값으로 hashkey, value HashMap 가져오기
    @Override
    public Map<?, ?> getEntries(String key) {
        Map<?, ?> entries = hashOperations.entries(key);
        return entries;
    }

    // 키-벨류 삭제
    @Override
    public void delData(String key) {
        redisTemplate.delete(key);
    }

    // 다음날 정각까지 남은 시간을 초단위로 계산
    private Duration getSecondsUntilTomorrow() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalTime nowTime = zonedDateTime.toLocalTime();
        LocalTime endTime = LocalTime.of(23, 59, 59);

        return Duration.between(nowTime, endTime).plusSeconds(10);
    }

    // 세션에서 나간 유저 id 삭제
    @Override
    public void delSessionParticipant(String sessionId, String memberId) {
        log.info("{} member in {} session is deleted", memberId, sessionId);
        setOperations.remove(sessionId, memberId);
    }

    @Override
    public void delSession(String sessionId) {
        log.info("{} session is deleted", sessionId);
        setOperations.pop(sessionId, getSessionParticipantCount(sessionId));
    }

    // 세션에 참여하고 있는 유저 수
    @Override
    public Long getSessionParticipantCount(String sessionId) {
        return setOperations.size(sessionId);
    }
}
