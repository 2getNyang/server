package com.project.nyang.global.common.S3;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * S3 컨트롤러입니다.
 *
 * @author : 선순주
 * @fileName : S3Controller
 * @since : 2025-07-10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity<List<String>> uploadFile(List<MultipartFile> multipartFiles){
        return ResponseEntity.ok(s3Service.uploadFile(multipartFiles));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFile(@RequestParam String fileName){
        s3Service.deleteFile(fileName);
        return ResponseEntity.ok(fileName);
    }
}