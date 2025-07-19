package com.project.nyang.global.security.oauth2;

import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.project.nyang.global.exception.ErrorCode.BAD_REQUEST;


/**
 * 카카오 로그인 연동해제에 대한 클래스입니다.
 *
 * @author : 오승훈
 * @fileName :
 * @since : 2025-07-18
 */
@Service
@RequiredArgsConstructor
public class OAuth2WithdrawKakaoService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * @param snsAccessToken 클라이언트에서 전달받은 카카오 액세스 토큰
     */
    public void unlinkKakao(String snsAccessToken) {
        // 1) 사용자 정보 조회
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders infoHeaders = new HttpHeaders();
        infoHeaders.setBearerAuth(snsAccessToken);
        infoHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Void> infoRequest = new HttpEntity<>(infoHeaders);

        ResponseEntity<Map> infoResponse = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                infoRequest,
                Map.class
        );

        if (infoResponse.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("카카오 사용자 정보 조회 실패: " + infoResponse.getBody());
        }

        // Map 의 구조를 보시면 "id" 가 최상위 키입니다.
        Number kakaoIdNum = (Number) infoResponse.getBody().get("id");
        String kakaoId = kakaoIdNum.toString();
        String loginId = kakaoId + "@kakao";  // User.loginId 와 동일한 형식

        // 2) 연동 해제(언링크)
        String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink";
        HttpHeaders unlinkHeaders = new HttpHeaders();
        unlinkHeaders.setBearerAuth(snsAccessToken);
        unlinkHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 바디는 없어도 됩니다.
        HttpEntity<Void> unlinkRequest = new HttpEntity<>(unlinkHeaders);

        ResponseEntity<Map> unlinkResponse = restTemplate.exchange(
                unlinkUrl,
                HttpMethod.POST,
                unlinkRequest,
                Map.class
        );

        if (unlinkResponse.getStatusCode() == HttpStatus.OK) {
            System.out.println("카카오 연동 해제 성공: " + unlinkResponse.getBody());

            // 3) User soft-delete 처리
            User user = userRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new IllegalArgumentException(BAD_REQUEST.getMessage()));

            user.markDeleted();
            user.deleteUsers();
            userRepository.save(user);

            System.out.println("User soft-delete 완료: deletedAt = " + user.getDeletedAt());
        } else {
            System.out.println("❌ 카카오 연동 해제 실패: " + unlinkResponse.getBody());
            throw new RuntimeException("카카오 연동 해제 실패");
        }
    }
}