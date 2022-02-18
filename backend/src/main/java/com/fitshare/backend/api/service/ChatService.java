package com.fitshare.backend.api.service;

import com.fitshare.backend.api.request.PrivateChatReq;
import com.fitshare.backend.api.response.PrivateChatRes;
import com.fitshare.backend.db.entity.PrivateChat;

import java.util.List;
import java.util.Map;

public interface ChatService {

    PrivateChat makePrivateChatEntity(Long memberId, PrivateChatReq privateChatReq);

    List<PrivateChat> getPrivateChatListFromEntries(String key, Map<?, ?> entries);

    List<PrivateChatRes> getPrivateChatResListFromEntries(String key, Map<?, ?> entries);

    void addPrivateChatInRedis(Long memberId, PrivateChatReq privateChatReq);

    void addPrivateChatListInMySql(List<PrivateChat> privateChatList);

    void checkChatList(Long memberId, Long friendId);

    List<PrivateChatRes> getPrivateChatListByDate(Long memberId, Long friendId, String date);

}
