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
import java.util.List;

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
    public void addPrivateChatInRedis(Long memberId, PrivateChatReq privateChatReq) {
        PrivateChat privateChat = makePrivateChatEntity(memberId, privateChatReq);

        String key = "chat_" + memberId + "_" + privateChatReq.getReceiverId() + "_" + privateChatReq.getCreatedTime();
        redisService.setData(key, privateChat);
    }

    @Override
    public void addPrivateChatInMySql(Long memberId, PrivateChatReq privateChatReq) {
        PrivateChat privateChat = makePrivateChatEntity(memberId, privateChatReq);
        privateChatRepository.save(privateChat);
    }

    @Override
    public void checkChat(Long memberId, Long friendId) {
        // friend가 보낸 메시지를 member가 읽는다
        privateChatRepository.updateIsChecked(friendId, memberId);
    }

    @Override
    public List<PrivateChatRes> getPrivateChatListByDate(Long memberId, Long friendId, LocalDate date) {
        String dateString = date.toString();
        if (date.isEqual(LocalDate.now())) {
            List<PrivateChatRes> privateChatRes = (List<PrivateChatRes>) redisService.getHashDataList("chat_" + memberId + "_" + friendId + "_" + dateString + "*");
            privateChatRes.addAll((List<PrivateChatRes>) redisService.getHashDataList("chat_" + friendId + "_" + memberId + "_" + dateString + "*"));
            return privateChatRes;
        } else {
            return privateChatRepository.findByCreatedTime(memberId, friendId, dateString);
        }
    }
}

