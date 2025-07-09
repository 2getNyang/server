package com.project.nyang.global.security.core;

import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 *
 * CustomUserDetailsService 클래스입니다.
 * @fileName        : CustomUserDetailsService
 * @author          : 오승훈
 * @since           : 2025-07-08
 *
 */

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    /**
     * Spring Security에서 기본적으로 사용하는 메서드입니다.
     * 여기서는 loginId 기준으로 사용자 조회를 합니다.
     * 로그인할 때 스프링에서 DB에 현재 로그인 하는 사용자가 존재하는지 확인하는 메서드
     */
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(loginId).orElseThrow(
                () -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다. -> "+loginId));
        return new CustomUserDetails(user);
    }


    /**
     * JWT 필터에서 userId 기반으로 인증 객체를 구성할 때 사용됩니다.
     */
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다. -> " + id));
        return new CustomUserDetails(user);
    }
}
