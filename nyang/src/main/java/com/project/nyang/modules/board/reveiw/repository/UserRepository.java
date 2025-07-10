package com.project.nyang.modules.board.reveiw.repository;

import com.project.nyang.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 임시 userRepository
 *
 * @author : 박세정
 * @fileName : UserRepository -> 추후 삭제 필요
 * @since : 2025-07-10
 */
public interface UserRepository  extends JpaRepository<User, Long> {
}