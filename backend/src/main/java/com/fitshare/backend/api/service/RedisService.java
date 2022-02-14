package com.fitshare.backend.api.service;

import java.util.Map;

public interface RedisService {

    void setData(String key, Object value);

    Object getData(String key);

    Map<?, ?> getEntries(String key);

    void delData(String key);

    void delSessionParticipant(String sessionId, String memberId);

    Long getSessionParticipantCount(String sessionId);

}
