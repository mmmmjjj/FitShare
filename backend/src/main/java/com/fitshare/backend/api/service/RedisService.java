package com.fitshare.backend.api.service;

public interface RedisService {

    void setData(String token, String id);

    String getData(String token);

    void delData(String token);

    void setDataExpire(String token,String id,long duration);

    Long getExpire(String token);
}
