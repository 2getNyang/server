package com.project.nyang.modules.auth.service;

import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.global.security.jwt.JwtTokenProvider;
import com.project.nyang.modules.auth.entity.Auth;
import com.project.nyang.modules.auth.repository.AuthRepository;
import com.project.nyang.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;

    private final JwtTokenProvider jwtTokenProvider;

    //아까 application.properties에서 추가한 환경변수(만료시간 이랑 리프레시 시간 늘려주는거)적용 후 변수로 생성
    @Value("${jwt.accessTokenExpirationTime}")
    private Long jwtAccessTokenExpirationTime;
    @Value("${jwt.refreshTokenExpirationTime}")
    private Long jwtRefreshTokenExpirationTime;


    //리프레시 토큰을 받아서 새로운 엑세스 토큰을 발급해주는 서비스
    @Transactional
    public String refreshToken(String refreshtoken) {
        //리프레시 토큰 유효성 검사
        if(jwtTokenProvider.validateToken(refreshtoken)) {
            Auth auth = authRepository.findByRefreshToken(refreshtoken).orElseThrow(
                    () -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

            //있으면 인증객체를 만들어서 새로운 토큰 발급
            String newAccessToken = jwtTokenProvider.generateToken(
                    new UsernamePasswordAuthenticationToken(
                            //아래 부분 새로운 토큰 발급 받을때 사용하는건데 이미 인증된 상태이므로 credentials null이어도 됨 
                            new CustomUserDetails(auth.getUser()), null), jwtAccessTokenExpirationTime); //엑세스 토큰 만료시간으로 설정

            auth.updateAccessToken(newAccessToken); //토큰 업데이트
            authRepository.save(auth); //DB에 반영

            return newAccessToken;
        }
        else{
            throw new CustomException(ErrorCode.REFRESH_TOKEN_INVALID);
        }
    }



    
}
