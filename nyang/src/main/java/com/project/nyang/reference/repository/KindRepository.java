package com.project.nyang.reference.repository;

import com.project.nyang.reference.dto.KindDTO;
import com.project.nyang.reference.dto.SubRegionDTO;
import com.project.nyang.reference.entity.Kind;
import com.project.nyang.reference.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.*;
import java.util.List;
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
    Optional<Kind> findByKindCd(String kindCd);

    List<Kind> findByUpKindCd_UpKindCd(String upKindCd);

    @Query("SELECT new com.project.nyang.reference.dto.KindDTO(kind.kindNm) " +
            "FROM Kind kind " +
            "WHERE kind.upKindCd.upKindNm = :upKindNm")
    List<KindDTO> findKindsByUpKind(String upKindNm);


}
