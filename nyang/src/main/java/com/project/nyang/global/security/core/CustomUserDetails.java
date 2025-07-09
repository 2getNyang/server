package com.project.nyang.global.security.core;

import com.project.nyang.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 *
 * CustomUserDetails 클래스입니다.
 * @fileName        : CustomUserDetails
 * @author          : 오승훈
 * @since           : 2025-07-08
 *
 */

@RequiredArgsConstructor //생성자 만들어주는 애노테이션 (밑에 private final User user 이거)
public class CustomUserDetails implements UserDetails {
    
    //UserDetails는 사용자 정보를 담는 인터페이스라서 이걸 우리가 커스텀해서 클래스로 사용
    // 로그인한 사용자의 정보를 담아두는 역할

    private final User user;

    //일반 사용자인지, 관리자인지 따로 구분은 안함
    //CustomUserDetails는 UserDetails를 구현한 클래스이므로
    //getAuthorities()는 반드시 오버라이드해야 하는 메서드여서 빈 내용이라도 반환!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
    
    //토큰에서 추출한 사용자 정보의 Id를 반환(테이블의 pk값)
    //User 엔티티에서 Id 추출
    public Long getId(){
        return user.getId();
    }


    @Override
    public String getPassword() {
        return null; //어짜피 일반 로그인 없어서 사용 x 그치만 오버라이딩은 해야하는 메서드라 null반환
    }

    @Override
    public String getUsername() {
        return user.getLoginId(); //(중복 안되는 값으로 해야함!)
    }

    /** 아래는 현재 계정 상태를 판단하는 메서드 **/
    @Override //현재 계정 상태가 활성화인지
    public boolean isEnabled() {
        return true; //여기 false면 아예 비활성화 된 계정이라고 판단해서 판단 자체를 안함
    }

    @Override //이 계정이 만료되었는지
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // 이 계정이 잠겨있는지
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override // 자격증명이 만료되지 않았는지
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
