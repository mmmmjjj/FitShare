package com.fitshare.backend.api.controller;

import com.fitshare.backend.api.request.ClothReq;
import com.fitshare.backend.api.response.ClothRes;
import com.fitshare.backend.api.service.ClothService;
import com.fitshare.backend.common.auth.JwtUtil;
import com.fitshare.backend.common.exception.handler.ErrorResponse;
import com.fitshare.backend.common.model.BaseResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.fitshare.backend.common.model.ResponseMessage.*;

@Api(value = "옷장 API", tags = {"Cloth"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clothes")
public class ClothController {

    private final ClothService clothService;

    @ApiOperation(value = "옷 추가",notes = "입력받은 옷 정보를 DB에 추가하는 API입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = ADD_CLOTH, response = ClothRes.class),
            @ApiResponse(code = 401, message = "권한 인증 오류", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Not Found 오류", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @PostMapping("")
    public ResponseEntity<BaseResponseBody> addCloth(@Valid @RequestBody ClothReq req) {
        Long memberId = JwtUtil.getCurrentId().orElseThrow(() -> new AccessDeniedException("Access denied"));
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.CREATED, ADD_CLOTH,clothService.addCloth(memberId,req)));
    }


    @ApiOperation(value = "옷 리스트", notes = "참가자 별 옷 목록을 반환하는 API입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = GET_CLOTHES_LIST, response = ClothRes.class),
            @ApiResponse(code = 401, message = "권한 인증 오류", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Not Found 오류", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @GetMapping("/{shoppingRoomId}/{memberId}")
    public ResponseEntity<BaseResponseBody> listClothes(@PathVariable Long shoppingRoomId, @PathVariable Long memberId) {

        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK,GET_CLOTHES_LIST,clothService.listClothes(memberId, shoppingRoomId)));
    }


    @ApiOperation(value = "옷 삭제",notes = "옷을 삭제하는 API입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = DELETE_CLOTH),
            @ApiResponse(code = 401, message = "권한 인증 오류", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Not Found 오류", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @DeleteMapping("/{clothId}")
    public ResponseEntity<BaseResponseBody> deleteCloth(@PathVariable Long clothId) {
        clothService.deleteCloth(clothId);
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK,DELETE_CLOTH));
    }


}
