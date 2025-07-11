package com.project.nyang.modules.like.repository;

import com.project.nyang.modules.animal.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 임시 AnimalRepository -> 삭제 필요
 *
 * @author : 박세정
 * @fileName : AnimalRepository
 * @since : 2025-07-10
 */
public interface TempAnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findByDesertionNo(String desertionNo);
}
