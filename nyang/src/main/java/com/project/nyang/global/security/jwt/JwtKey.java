package com.project.nyang.global.security.jwt;


import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

/**
 *
 * JwtKey 클래스입니다.
 * @fileName        : JwtKey
 * @author          : 오승훈
 * @since           : 2025-07-08
 *
 */

//설정용 파일이란 뜻
@Configuration
public class JwtKey {

    @Value("${jwt.secretKey}")
    private  String secretKey;

    //서명키를 만들어서 반환하는 메서드
    @Bean
    public SecretKey secretKey(){
        byte[] keyBytes = secretKey.getBytes(); // 설정파일에서 불러온 키 값을 바이트 배열로 변환
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
