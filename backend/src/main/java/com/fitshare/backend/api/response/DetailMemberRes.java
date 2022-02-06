package com.fitshare.backend.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@ApiModel("MemberResponse")
@Getter
@Setter
@AllArgsConstructor
public class DetailMemberRes {

    private BaseMemberRes baseMemberRes;

    @ApiModelProperty(name = "전화번호", example = "010-1234-5678")
    private String phone;

    @ApiModelProperty(name = "가입 시각", example = "2022-02-03T17:20:52")
    private LocalDateTime createdTime;

    @ApiModelProperty(name = "마지막 정보 수정 시각", example = "2022-02-03T17:20:52")
    private LocalDateTime updatedTime;

}
