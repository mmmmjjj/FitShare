package com.fitshare.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel("ChatRequest")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatReq {

    @ApiModelProperty(name = "쇼핑룸 Id", example = "1")
    private int roomId;

    @ApiModelProperty(name = "메시지 전송자 이름", example = "홍길동")
    private String senderName;

    @ApiModelProperty(name = "전송한 메시지 내용", example = "Hello")
    private String message;

}
