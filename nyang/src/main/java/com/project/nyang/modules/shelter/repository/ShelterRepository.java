package com.project.nyang.modules.shelter.repository;

import com.project.nyang.modules.shelter.dto.ShelterListDTO;
import com.project.nyang.modules.shelter.entity.Shelter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * ShelterRepository에 대한 클래스입니다.
 *
 * @author : 오승훈
 * @fileName : ShelterRepository
 * @since : 2025-07-10
 */
public interface ShelterRepository extends JpaRepository<Shelter, String> {

    /**
     * 보호소 전체 목록을 조회하는 JPQL 쿼리입니다.
     * Shelter와 연관된 Region, SubRegion 테이블을 JOIN하여
     * 보호소 이름(careName), 전화번호(careTel), 시도명(regionName), 시군구명(subRegionName)을 가져옵니다.
     * 결과는 ShelterListDTO에 매핑되며, 페이징 처리를 지원합니다.
     *
     * - 주의: 이 쿼리는 명시적 JOIN을 사용하므로 N+1 문제가 발생하지 않습니다.
     */
    @Query("SELECT new com.project.nyang.modules.shelter.dto.ShelterListDTO(s.careName, s.careTel, r.regionName, sr.subRegionName) " +
            "FROM Shelter s " +
            "JOIN s.region r " +
            "JOIN s.subRegion sr")
    Page<ShelterListDTO> findAllShelters(Pageable pageable);
}
