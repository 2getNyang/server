package com.project.nyang.modules.animal.repository;

import com.project.nyang.modules.animal.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : 이지은
 * @fileName : AnimalDetailRepository
 * @since : 25. 7. 9.
 */
public interface AnimalDetailRepository extends JpaRepository<Animal, String> {

    Optional<Animal> findByDesertionNo(String desertionNo);

}
