package com.fitshare.backend.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatReq {

    // 메시지 전송자 이름
    private String senderName;

    // 전송한 메시지 내용
    private String message;

    // 메시지 전송 시각
    private String createdTime;

}
