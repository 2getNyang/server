//package com.project.nyang.global.security.oauth2;
//
//import com.project.nyang.modules.user.entity.User;
//import com.project.nyang.modules.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
//import static com.project.nyang.global.exception.ErrorCode.BAD_REQUEST;
//
///**
// * 소셜로그인 연동해제
// *
// * @author : 이은서
// * @fileName : OAuth2UnlinkService
// * @since : 25. 7. 11.
// */
//@Service
//@RequiredArgsConstructor
//public class OAuth2UnlinkService {
//
//    private final UserRepository userRepository;
//
//    @Value("${NAVER_CLIENT_ID}")
//    private String NAVER_CLIENT_ID;
//
//    @Value("${NAVER_CLIENT_SECRET}")
//    private String NAVER_CLIENT_SECRET;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public void unlinkNaver(String accessToken) {
//
//        // 네이버 사용자 정보 조회 (https://openapi.naver.com/v1/nid/me)
//        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
//        HttpHeaders infoHeaders = new HttpHeaders();
//        infoHeaders.setBearerAuth(accessToken);
//        HttpEntity<Void> infoRequest = new HttpEntity<>(infoHeaders);
//
//        ResponseEntity<Map> infoResponse = restTemplate.exchange(
//                userInfoUrl,
//                HttpMethod.GET,
//                infoRequest,
//                Map.class
//        );
//
//        if (infoResponse.getStatusCode() != HttpStatus.OK) {
//            throw new RuntimeException("네이버 사용자 정보 조회 실패: " + infoResponse.getBody());
//        }
//
//        Map<String, Object> responseMap = (Map<String, Object>) infoResponse.getBody().get("response");
//        String naverId = responseMap.get("id").toString();
//        String loginId = naverId + "@naver"; // User.loginId와 동일한 형식
//
//        // 2️⃣ 네이버 연동 해제(토큰 폐기)
//        String url = "https://nid.naver.com/oauth2.0/token";
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "delete");
//        params.add("client_id", NAVER_CLIENT_ID);
//        params.add("client_secret", NAVER_CLIENT_SECRET);
//        params.add("access_token", accessToken);
//        params.add("service_provider", "NAVER");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            System.out.println("✅ 네이버 연동 해제(토큰 폐기) 성공: " + response.getBody());
//
//            // 3️⃣ User 조회 및 deletedAt timestamp 처리
//            User user = userRepository.findByLoginId(loginId)
//                    .orElseThrow(() -> new IllegalArgumentException(BAD_REQUEST.getMessage()));
//
//            user.markDeleted();
//            userRepository.save(user);
//
//            System.out.println("✅ User soft-delete 완료: deletedAt = " + user.getDeletedAt());
//        } else {
//            System.out.println("❌ 네이버 연동 해제(토큰 폐기) 실패: " + response.getBody());
//            throw new RuntimeException("네이버 연동 해제 실패");
//        }
//    }
//}
