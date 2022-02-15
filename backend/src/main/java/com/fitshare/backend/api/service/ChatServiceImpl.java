package com.fitshare.backend.api.service;

import com.fitshare.backend.api.request.PrivateChatReq;
import com.fitshare.backend.api.response.PrivateChatRes;
import com.fitshare.backend.common.exception.MemberNotFoundException;
import com.fitshare.backend.db.entity.Member;
import com.fitshare.backend.db.entity.PrivateChat;
import com.fitshare.backend.db.repository.MemberRepository;
import com.fitshare.backend.db.repository.PrivateChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final PrivateChatRepository privateChatRepository;
    private final MemberRepository memberRepository;
    private final RedisService redisService;

    @Override
    public PrivateChat makePrivateChatEntity(Long memberId, PrivateChatReq privateChatReq) {
        Member sender = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Member receiver = memberRepository.findById(Long.valueOf(privateChatReq.getReceiverId())).orElseThrow(() -> new MemberNotFoundException(memberId));

        PrivateChat privateChat = PrivateChat.builder()
                .sender(sender)
                .receiver(receiver)
                .message(privateChatReq.getMessage())
                .createdTime(privateChatReq.getCreatedTime())
                .build();

        return privateChat;
    }

    @Override
    public List<PrivateChat> getPrivateChatListFromEntries(String key, Map<?, ?> entries) {
        List<PrivateChat> privateChatList = new ArrayList<>();

        String[] keyArray = key.split("_");
        Long senderId = Long.valueOf(keyArray[1]);
        Long receiverId = Long.valueOf(keyArray[2]);

        Member sender = memberRepository.findById(senderId).orElseThrow(() -> new MemberNotFoundException(senderId));
        Member receiver = memberRepository.findById(receiverId).orElseThrow(() -> new MemberNotFoundException(receiverId));

        BiConsumer<Object, Object> biConsumer = (hashKey, value) -> {
            PrivateChat privateChat = PrivateChat
                    .builder()
                    .sender(sender)
                    .receiver(receiver)
                    .message((String) value)
                    .createdTime(LocalDateTime.parse(hashKey.toString()))
                    .build();

            privateChatList.add(privateChat);
        };

        entries.forEach(biConsumer);

        return privateChatList;
    }

    @Override
    public List<PrivateChatRes> getPrivateChatResListFromEntries(String key, Map<?, ?> entries) {
        List<PrivateChatRes> privateChatResList = new ArrayList<>();

        String[] keyArray = key.split("_");
        Long senderId = Long.valueOf(keyArray[1]);
        Long receiverId = Long.valueOf(keyArray[2]);

        BiConsumer<Object, Object> biConsumer = (hashKey, value) -> {
            PrivateChatRes privateChatRes = PrivateChatRes
                    .builder()
                    .senderId(senderId)
                    .receiverId(receiverId)
                    .message((String) value)
                    .createdTime(LocalDateTime.parse(hashKey.toString()))
                    .build();

            privateChatResList.add(privateChatRes);
        };

        entries.forEach(biConsumer);

        return privateChatResList;
    }

    @Override
    public void addPrivateChatInRedis(Long memberId, PrivateChatReq privateChatReq) {
        PrivateChat privateChat = makePrivateChatEntity(memberId, privateChatReq);

        String nowDate = "" + privateChat.getCreatedTime().getYear() + "-";
        nowDate += privateChat.getCreatedTime().getMonthValue() < 10 ? "0" + privateChat.getCreatedTime().getMonthValue() : privateChat.getCreatedTime().getMonthValue();
        nowDate += "-" + privateChat.getCreatedTime().getDayOfMonth();

        String key = "chat_" + memberId + "_" + privateChatReq.getReceiverId() + "_" + nowDate;
        redisService.setData(key, privateChat);
    }

    @Override
    public void addPrivateChatListInMySql(List<PrivateChat> privateChatList) {
        privateChatRepository.saveAll(privateChatList);
    }

    @Override
    public void checkChatList(Long memberId, Long friendId) {
        // friend가 보낸 메시지를 member가 읽는다
        privateChatRepository.updateIsChecked(friendId, memberId);
    }

    @Override
    public List<PrivateChatRes> getPrivateChatListByDate(Long memberId, Long friendId, LocalDate date) {
        String dateString = date.toString();

        if (date.isEqual(LocalDate.now())) { // 조회 요청 날짜가 오늘이면 Redis에서 조회
            String key = "chat_" + memberId + "_" + friendId + "_" + dateString;
            List<PrivateChatRes> privateChatRes = getPrivateChatResListFromEntries(key, redisService.getEntries(key));

            key = "chat_" + friendId + "_" + memberId + "_" + dateString;
            privateChatRes.addAll(getPrivateChatResListFromEntries(key, redisService.getEntries(key)));

            return privateChatRes;
        } else { // 조회 요청 날짜가 오늘 전이면 MySQL에서 조회
            return privateChatRepository.findByCreatedTime(memberId, friendId, dateString);
        }
    }
}

