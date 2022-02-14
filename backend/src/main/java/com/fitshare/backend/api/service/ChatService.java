package com.fitshare.backend.api.service;

import com.fitshare.backend.api.request.PrivateChatReq;
import com.fitshare.backend.api.response.PrivateChatRes;
import com.fitshare.backend.db.entity.PrivateChat;

import java.time.LocalDate;
import java.util.List;

public interface ChatService {

    PrivateChat makePrivateChatEntity(Long memberId, PrivateChatReq privateChatReq);

    void addPrivateChatInRedis(Long memberId, PrivateChatReq privateChatReq);

    void addPrivateChatInMySql(Long memberId, PrivateChatReq privateChatReq);

    void checkChat(Long memberId, Long friendId);

    List<PrivateChatRes> getPrivateChatListByDate(Long memberId, Long friendId, LocalDate date);

}
