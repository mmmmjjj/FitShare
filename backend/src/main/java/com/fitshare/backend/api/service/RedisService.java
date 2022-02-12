package com.fitshare.backend.api.service;

import java.util.Set;

public interface RedisService {

    void setData(String token, String id);

    String getData(String token);

    void delData(String token);

    void setDataExpire(String token,String id,long duration);

    Long getExpire(String token);

    void setSessionParticipant(String sessionId, String memberId);

    Set<String> getSessionParticipants(String sessionId);

    void delSessionParticipant(String sessionId, String memberId);

    Long getSessionParticipantCount(String sessionId);
}
