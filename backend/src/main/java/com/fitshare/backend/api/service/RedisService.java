package com.fitshare.backend.api.service;

import java.util.Set;

public interface RedisService {

    void setData(String key, Object value);

    Object getData(String key);

    void delData(String key);

    void setDataExpire(String key, Object value, long duration);

    Long getExpire(String key);

    Set<Object> getSessionParticipants(String sessionId);

    void delSessionParticipant(String sessionId, String memberId);

    Long getSessionParticipantCount(String sessionId);

}
