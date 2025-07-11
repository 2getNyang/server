package com.project.nyang.modules.user.repository;

import com.project.nyang.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * userRepository입니다.
 *
 * @author : 오승훈
 * @fileName : UserRepository
 * @since : 2025-07-08
 */
public interface UserRepository extends JpaRepository<User, Long> {

    //loginId로 사용자 조회를 합니다.
    Optional<User> findByLoginId(String loginid);

}