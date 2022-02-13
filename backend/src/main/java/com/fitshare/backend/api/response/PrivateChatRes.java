package com.fitshare.backend.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@ApiModel("ChatResponse")
@Getter
@Setter
@AllArgsConstructor
public class PrivateChatRes {

    @ApiModelProperty(name = "메시지를 보낸 사람")
    private BaseMemberRes sender;

    @ApiModelProperty(name = "메시지를 받은 사람")
    private BaseMemberRes receiver;

    @ApiModelProperty(name = "메시지 내용", example = "hello")
    private String message;

    @ApiModelProperty(name = "메시지를 받은 사람이 메시지를 읽었는지 여부", example = "false")
    private Boolean isChecked;

    @ApiModelProperty(name = "메시지 전송 시각", example = "2022-02-03T17:20:52")
    private LocalDateTime createdTime;

}
