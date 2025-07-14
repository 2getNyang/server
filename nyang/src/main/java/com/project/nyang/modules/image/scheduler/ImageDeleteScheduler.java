package com.project.nyang.modules.image.scheduler;

import com.project.nyang.global.common.S3.S3Service;
import com.project.nyang.modules.image.entity.Image;
import com.project.nyang.modules.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * s3 이미지 스케줄러입니다. 삭제된지 3일이 지난 게시글과 관련된 이미지를 삭제합니다.
 *
 * @author : 선순주
 * @fileName : ImageDeleteScheduler
 * @since : 2025-07-13
 */
@RequiredArgsConstructor
@Service
public class ImageDeleteScheduler {
    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    @Scheduled(cron = "0 0 1 * * *") // 매일 새벽 1시에 실행
    public void cleanUpOldImages() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(1); //소프트 삭제된지 1일이 지난 이미지 데이터 추출
        LocalDateTime threshold2 = LocalDateTime.now().minusMinutes(1); //테스트용. 소프트 삭제된지 1분이 지난 이미지 데이터 추출
        List<Image> images = imageRepository.findByDeletedAtIsNotNullAndDeletedAtBefore(threshold);

        for (Image image : images) {
            String fileName = extractFileNameFromUrl(image.getS3Url());
            s3Service.deleteFile(fileName);
        }

        imageRepository.deleteAll(images); // DB에서 물리 삭제
    }

    //s3 이미지 경로에서 이미지 명 +확장자 추출
    private String extractFileNameFromUrl(String s3Url) {
        int index = s3Url.indexOf(".com/");
        if (index != -1) {
            return s3Url.substring(index + 5);
        } else {
            throw new IllegalArgumentException("Invalid S3 URL: " + s3Url);
        }
    }
}
