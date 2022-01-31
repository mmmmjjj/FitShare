package com.fitshare.backend.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDTO {
    private int roomId;
    private int senderId;
    private int receiverId;
    private String message;
    private int isChecked;
    private String createdTime;
}
