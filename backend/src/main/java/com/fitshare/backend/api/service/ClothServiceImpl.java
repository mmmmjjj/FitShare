package com.fitshare.backend.api.service;

import com.fitshare.backend.api.request.ClothReq;
import com.fitshare.backend.api.response.ClothRes;
import com.fitshare.backend.common.exception.ImageFailedException;
import com.fitshare.backend.db.entity.Cloth;
import com.fitshare.backend.db.entity.RoomParticipant;
import com.fitshare.backend.db.repository.ClothRepository;
import com.fitshare.backend.db.repository.RoomParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClothServiceImpl implements ClothService {

    private final ClothRepository clothRepository;
    private final RoomParticipantRepository roomParticipantRepository;

    private final S3ServiceImpl s3Service;

    private static final String PYTHON_PATH = "/deploy/img_trans.py";

    /**
     * 옷 추가
     **/
    @Override
    public ClothRes addCloth(Long memberId, ClothReq req) {

        Long shoppingRoomId = req.getShoppingRoomId();
        String imageUrl = req.getImageUrl();

        // python backend/img_trans.py "imagUrl" "저장할 경로" "이미지 타이틀"
        // 이미지타이틀 : 쇼핑룸id_멤버id_생성시간.png
        RoomParticipant roomParticipant = roomParticipantRepository.findByMemberIdAndShoppingRoomId(memberId, shoppingRoomId).orElse(null);

        // 이미지 저장 경로, 제목 지정
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd_HHmmss");

        String imagePath = "/data";
        String imageTitle = shoppingRoomId + "_" + memberId + "_" + time.format(System.currentTimeMillis()) + ".png";

        ProcessBuilder builder = new ProcessBuilder("python3", PYTHON_PATH, imageUrl, imagePath, imageTitle);

        String clothUrl = null;
        try {
            Process process = builder.start();
            // 파이썬 프로세스가 종료 될 때 까지 기다린다.
            int exitval = process.waitFor();

            if (exitval != 0) {
                // 비정상 종료
                log.debug("{} 이미지 프로세스가 비정상적으로 종료되었습니다.", imageTitle);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        File transImage = new File("/data/" + imageTitle);

        boolean isExists = transImage.exists();

        // 이미지 생성에 실패 시 에러 반환
        if(!isExists) throw new ImageFailedException();

        // S3 서버에 업로드
        clothUrl = s3Service.upload(transImage, "images");

        Cloth cloth = Cloth.builder()
                .roomParticipant(roomParticipant)
                .clothUrl(clothUrl)
                .clothPath("images/"+imageTitle)
                .build();

        // 옷 정보 DB 저장
        clothRepository.save(cloth);

        return new ClothRes(cloth.getId(), cloth.getClothUrl());
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
        Cloth cloth = clothRepository.getById(clothId);
        s3Service.delete(cloth.getClothPath());
        clothRepository.deleteById(clothId);
    }


}
