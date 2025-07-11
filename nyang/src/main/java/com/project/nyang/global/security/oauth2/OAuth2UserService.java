package com.project.nyang.global.security.oauth2;

import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.global.security.jwt.JwtTokenProvider;
import com.project.nyang.modules.auth.entity.Auth;
import com.project.nyang.modules.auth.repository.AuthRepository;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//카카오 로그인창 이동 경로
//localhost:8080/oauth2/authorization/kakao

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    //DefaultOAuth2UserService spring Security에서 기본적으로 제공하는 Oauth2 인증 정보를 처리하는 클래스

    private final AuthRepository authRepository;   // JWT 저장용 (access, refresh)
    private final UserRepository userRepository;   // 회원 정보 DB 접근
    private final JwtTokenProvider jwtTokenProvider; // JWT 발급기

    @Value("${jwt.accessTokenExpirationTime}")
    private Long jwtAccessTokenExpirationTime;
    @Value("${jwt.refreshTokenExpirationTime}")
    private Long jwtRefreshTokenExpirationTime;



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //소셜 로그인 성공 시 사용자 정보를 구글이나 카카오에서 받아서 처리하는 메서드

        // 기본 OAuth2User 정보 가져오기 (email, name 등)
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 어떤 소셜 로그인 제공자인지 (google, kakao 등)
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // 소셜 사용자 정보 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String loginId, name, email;


        // provider 별로 파싱 방식이 다름
        if ("kakao".equals(provider)) {

            loginId = attributes.get("id").toString() + "@kakao"; //고유 id + 구분자
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            name = (String) profile.get("nickname");

        }else if("naver".equals(provider)){

            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            loginId = response.get("id").toString() + "@naver"; // 고유 id + 구분자
            email = (String) response.get("email");
            //여기서 나타내는 name 이 닉네임 맞겠죠?
            name = (String) response.get("nickname");
        } else {
            // 기타 provider 처리 안함
            email = null;
            name = null;
            loginId = null;

        }

        System.out.println(provider+" 로그인 확인 loginId = "+loginId);
        System.out.println(provider+" 로그인 확인 email = "+email);
        System.out.println(provider+" 로그인 확인 name = "+ name);

        // 회원 정보가 DB에 존재하는지 확인
        User user = userRepository.findByLoginId(loginId)
                .orElseGet(() -> {
                    //회원이 없다면 자동 회원가입 처리
                    User newUser = User.builder()
                            .loginId(loginId)
                            .loginType(provider)  // 필요시 구분용
                            .nickname(name != null ? name : "소셜유저")
                            .email(email != null ? email : "")
                            .build();


                    return userRepository.save(newUser);   // 회원가입 저장
                });

        // 시큐리티에서 사용할 인증 객체 생성
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        customUserDetails,
                        null,
                        Collections.singleton(new SimpleGrantedAuthority("USER")));


        // JWT 액세스 & 리프레시 토큰 발급
        String accessToken = jwtTokenProvider.generateToken(authentication,jwtAccessTokenExpirationTime);
        String refreshToken = jwtTokenProvider.generateToken(authentication,jwtRefreshTokenExpirationTime);


        // JWT는 SuccessHandler에서 쿠키/쿼리로 전달 → 여기선 속성에만 담아 둠
        // 프론트에 쿠키로 넘겨주는것
        Map<String, Object> customAttributes = new HashMap<>(attributes);
        customAttributes.put("accessToken", accessToken);
        customAttributes.put("refreshToken", refreshToken);
        customAttributes.put("name", name);
        customAttributes.put("id", user.getId()); // ← PK(id) 추가

        // Auth 엔티티에 토큰 저장 (User와 1:1 매핑)
        // 카카오, 구글에서 제공받은 토큰을 authRepository를 통해 저장
        Optional<Auth> optionalAuth = authRepository.findByUser(user);
        if (optionalAuth.isPresent()) {
            Auth auth = optionalAuth.get();
            auth.updateAccessToken(accessToken);
            auth.updateRefreshToken(refreshToken);
            authRepository.save(auth); // 반드시 저장!
        } else {
            Auth auth = Auth.builder()
                    .user(user)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .build();

            authRepository.save(auth);
        }

        // 최종적으로 Spring Security에 전달할 OAuth2User 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")), // 권한
                customAttributes,  // 속성 정보 (JWT 포함)
                "id" // PK로 사용할 식별자 (프론트에서도 사용할 수 있음)
        );
    }
}