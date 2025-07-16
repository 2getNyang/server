package com.project.nyang.global.security.oauth2;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * custom oauth2user service 소셜에서 제공하는 access token 을 추출해서 넣는 서비스
 *
 * @author : 이은서
 * @fileName : CustomOAuth2UserService
 * @since : 25. 7. 16.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest){
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

        // ⭐ 진짜 소셜 access token
        String snsAccessToken = userRequest.getAccessToken().getTokenValue();

        // ✅ attributes에 추가해줘야 Handler에서 꺼낼 수 있음
        attributes.put("snsAccessToken", snsAccessToken);

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                attributes,
                "id" // 플랫폼마다 key 다름: naver는 "id", kakao는 "sub" 또는 "kakao_account"
        );
    }
}
