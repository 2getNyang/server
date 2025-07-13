package com.project.nyang.modules.auth.entity;

import com.project.nyang.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

/**
 *
 * Auth 엔티티 클래스입니다. 
 * @fileName        : Auth
 * @author          : 오승훈
 * @since           : 2025-07-08
 *
 */

@AllArgsConstructor //모든 필드를 파라미터로 자동생선
@NoArgsConstructor // 기본 생성자를 자동 생성
@Getter
@Entity //DB 테이블과 자바 객체를 연결
@Builder
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id", nullable = false)
    private Long id;

    @Column(name = "token_type",nullable = false) //테이블의 컬럼을 자바 필드와 연결
    private String tokenType;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "refresh_token",nullable = false)
    private String refreshToken;

    //테이블과 테이블을 연결      (1대1 관계에서는 연관관계 주인쪽만 패치 전략이 적용됨)
    @OneToOne(fetch = FetchType.LAZY) //지연로딩 적용 -> Auth 엔티티 조회할 때 user 객체는 불러오지 않음
    @JoinColumn(name = "user_id")
    private User user;


    // updateAccessToken 메서드 추가
    //토큰값을 업데이트 해주는 메서드
    public void updateAccessToken(String newAccessToken) {
        this.accessToken = newAccessToken;
    }

// 나중에 oauth 카카오 소셜로그인 할 떄 사용할 거 같음
    // updateRefreshToken 메서드 추가
    public void updateRefreshToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }
}
