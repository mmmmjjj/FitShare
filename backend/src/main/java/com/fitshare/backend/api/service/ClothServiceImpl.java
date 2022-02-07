package com.fitshare.backend.api.service;

import com.fitshare.backend.api.request.ClothReq;
import com.fitshare.backend.api.response.ClothRes;
import com.fitshare.backend.db.entity.Cloth;
import com.fitshare.backend.db.entity.RoomParticipant;
import com.fitshare.backend.db.repository.ClothRepository;
import com.fitshare.backend.db.repository.RoomParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClothServiceImpl implements ClothService {

    private final ClothRepository clothRepository;
    private final RoomParticipantRepository roomParticipantRepository;

    private static final String PYTHON_PATH = "img_trans.py";

    /**
     * 옷 추가
     **/
    @Override
    public ClothRes addCloth(Long memberId, ClothReq req) {

        Long shoppingRoomId = req.getShoppingRoomId();
        String imageUrl = req.getImageUrl();

        // python backend/img_trans.py "imagUrl" "저장할 경로" "이미지 타이틀"
        // 이미지타이틀 : 쇼핑룸id_멤버id_생성시간.png
        RoomParticipant roomParticipant = roomParticipantRepository.findByMemberId(memberId).orElse(null);

        // 이미지 저장 경로, 제목 지정
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd_HHmmss");

        String imagePath = "/data/"+shoppingRoomId;
        String imageTitle = shoppingRoomId+"_"+memberId+"_"+time.format(System.currentTimeMillis());

        ProcessBuilder builder = new ProcessBuilder("python3", PYTHON_PATH, imageUrl, imagePath, imageTitle);

        try {
            Process process = builder.start();

            int exitval = process.waitFor(); // 파이썬 프로세스가 종료 될 때 까지 기다린다.

            if(exitval != 0) {
                // 비정상 종료
                log.debug("{} 이미지 프로세스가 비정상적으로 종료되었습니다.",imageTitle);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Cloth cloth = Cloth.builder()
                .roomParticipant(roomParticipant)
                .clothUrl("프론트 기준 경로"+shoppingRoomId+"/"+imageTitle)
                .build();

        // 옷 정보 DB 저장
        clothRepository.save(cloth);

        return new ClothRes(cloth.getId(),cloth.getClothUrl());
    }

    /**
     * 멤버별 옷 리스트
     **/
    @Override
    public List<ClothRes> listClothes(Long memberId, Long shoppingRoomId) {

        return clothRepository.getClothByMemberId(memberId,shoppingRoomId).orElse(null);
    }

    /**
     * 옷 삭제
     **/
    @Override
    public void deleteCloth(Long clothId) {
        clothRepository.deleteById(clothId);
    }


}
