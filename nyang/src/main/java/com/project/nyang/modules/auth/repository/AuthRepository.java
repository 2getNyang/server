package com.project.nyang.modules.auth.repository;

import com.project.nyang.modules.auth.entity.Auth;
import com.project.nyang.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * AuthRepository 클래스입니다.
 * @fileName        : AuthRepository
 * @author          : 오승훈
 * @since           : 2025-07-09
 *
 */

@Repository //리포지토리를 담당하는 클래스라는것 명시
public interface AuthRepository extends JpaRepository<Auth, Long> {
    
    Optional<Auth> findByRefreshToken(String refreshToken);

    Optional<Auth> findByUser(User user);

}
