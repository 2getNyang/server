package com.project.nyang.reference.repository;

import com.project.nyang.reference.entity.UpKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 축종 레포지토리입니다.
 *
 * @author : 선순주
 * @fileName : UpkindRepository
 * @since : 2025-07-11
 */
public interface UpkindRepository extends JpaRepository<UpKind,String> {
    Optional<UpKind> findByUpKindNm(String upKindNm);
    Optional<UpKind> findByUpKindCd(String upKindCd);

    /**
     * 모든 축종이름을 조회하는 쿼리
     */
    @Query("SELECT uk.upKindNm FROM UpKind uk")
    List<String> findAllUpKinds();
}
