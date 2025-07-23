package com.project.nyang.global.security.oauth2;

import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.project.nyang.global.exception.ErrorCode.BAD_REQUEST;

/**
 * 구글 회원탈퇴 처리하는 서비스입니다
 *
 * @author : 이지은
 * @fileName : OAuth2WithdrawGoogleService
 * @since : 25. 7. 21.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2WithdrawGoogleService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * @param snsAccessToken 클라이언트에서 전달받은 구글 액세스 토큰
     */
    public void unlinkGoogle(String snsAccessToken) {
        // 1. 구글 사용자 정보 조회
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
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
            throw new RuntimeException("구글 사용자 정보 조회 실패: " + infoResponse.getBody());
        }

        // sub 값 = 고유 식별자
        String googleId = infoResponse.getBody().get("sub").toString();
        String loginId = googleId + "@google";

        // 2. 구글 연동 해제 (revoke)
        String revokeUrl = "https://oauth2.googleapis.com/revoke?token=" + snsAccessToken;
        HttpHeaders revokeHeaders = new HttpHeaders();
        revokeHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Void> revokeRequest = new HttpEntity<>(revokeHeaders);

        ResponseEntity<String> revokeResponse = restTemplate.exchange(
                revokeUrl,
                HttpMethod.POST,
                revokeRequest,
                String.class
        );

        if (revokeResponse.getStatusCode() == HttpStatus.OK) {
            log.info("✅ 구글 연동 해제 성공");

            // 3. User soft-delete 처리
            User user = userRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new IllegalArgumentException(BAD_REQUEST.getMessage()));

            user.markDeleted();
            user.deleteUsers();
            userRepository.save(user);

            log.info("✅ User soft-delete 완료: deletedAt = {}", user.getDeletedAt());

        } else {
            log.warn("❌ 구글 연동 해제 실패: {}", revokeResponse.getBody());
            throw new RuntimeException("구글 연동 해제 실패");
        }
    }
}