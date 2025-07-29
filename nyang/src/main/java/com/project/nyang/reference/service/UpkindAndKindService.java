package com.project.nyang.reference.service;

import com.project.nyang.reference.dto.KindDTO;
import com.project.nyang.reference.dto.UpKindDTO;
import com.project.nyang.reference.entity.Kind;
import com.project.nyang.reference.repository.KindRepository;
import com.project.nyang.reference.repository.UpkindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 축종, 품종 검색 필터링 서비스입니다.
 *
 * @author : 선순주
 * @fileName : UpkindAndKindService
 * @since : 2025-07-22
 */
@Service
@RequiredArgsConstructor
public class UpkindAndKindService {
    private final UpkindRepository upkindRepository;
    private final KindRepository kindRepository;

    /**
     * 모든 축종 조회
     */
    @Transactional
    public List<UpKindDTO> getAllUpKinds() {
        return upkindRepository.findAllUpKinds().stream()
                .map(upKindName -> new UpKindDTO(upKindName))
                .toList();
    }

    /**
     * 선택된 축종에 해당하는 품종 목록 조회.
     */
    @Transactional
    public List<KindDTO> getKindsByUpKind(String upKindNm){
        return kindRepository.findKindsByUpKind(upKindNm);
    }
}