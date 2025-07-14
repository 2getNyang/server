package com.project.nyang.reference.repository;

import com.project.nyang.reference.entity.UpKind;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
