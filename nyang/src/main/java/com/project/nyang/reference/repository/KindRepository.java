package com.project.nyang.reference.repository;

import com.project.nyang.reference.entity.Kind;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;
import java.util.Optional;

/**
 * 품종 레포지토리입니다.
 *
 * @author : 선순주
 * @fileName : KindRepository
 * @since : 2025-07-11
 */
public interface KindRepository extends JpaRepository<Kind,String> {
    Optional<Kind> findByKindNm(String kindNm);
}
