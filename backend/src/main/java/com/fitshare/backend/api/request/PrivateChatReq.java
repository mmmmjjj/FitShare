package com.fitshare.backend.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivateChatReq extends ChatReq {

    private Long receiverId;

}
