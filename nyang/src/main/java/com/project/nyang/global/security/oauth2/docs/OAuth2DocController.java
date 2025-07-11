package com.project.nyang.global.security.oauth2.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 소셜 로그인 관련 설명 Swagger 문서 클래스입니다.
 *
 * @author : 오승훈
 * @fileName : OAuth2DocController
 * @since : 2025-07-10
 */
@Tag(name = "🌐 소셜 로그인 API", description = "카카오 로그인 설명용 API 문서입니다.")
@RestController
@RequestMapping("/oauth2/docs")
public class OAuth2DocController {

    @Operation(summary = "카카오 로그인", description = """
            카카오 로그인은 아래 URL로 이동하여 OAuth2 인증을 시작합니다.  
            프론트에서는 [GET] 요청으로 아래 링크로 이동시켜 주세요.  
            성공적으로 로그인하면 accessToken, refreshToken 이 담긴 쿠키가 발급됩니다.
            
            🔗 요청 URL: \s
                    - GET `{호스트주소}/oauth2/authorization/kakao` \s
                    - 예시: `http://localhost:8080/oauth2/authorization/kakao`
            
            ✅ 참고 사항:
            - 별도의 파라미터 필요 없음
            - 로그인 성공 후 자동으로 SuccessHandler가 동작
            """)
    @GetMapping("/authorization/kakao")
    public ResponseEntity<Void> kakaoLoginDoc() {
        return ResponseEntity.ok().build(); // 설명용 Swagger용 API — 실제로 사용되지 않음
    }

    @Operation(summary = "네이버 로그인", description = "네이버 로그인또한 " +
            "`http://localhost:8080/oauth2/authorization/naver`" +
            "로 이동하면 로그인-회원가입 처리가 됩니다.")
    @GetMapping("/authorization/naver")
    public ResponseEntity<Void> naverLoginDoc() {
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "소셜 로그아웃", description = """
            소셜 로그아웃은 아래 URL로 [GET] 요청하여 브라우저의 accessToken, refreshToken 쿠키를 삭제합니다.
            OAuth2 로그아웃 SuccessHandler가 동작하여 쿠키 삭제 후 리다이렉트됩니다.
            
            🔗 요청 URL:
            - GET `{호스트주소}/api/auth/logout`
            - 예시: `http://localhost:8080/api/auth/logout`

            ✅ 참고 사항:
            - 로그아웃 시 자동으로 쿠키가 삭제됩니다.
            """)
    @GetMapping("/logout")
    public ResponseEntity<Void> logoutDoc() {
        return ResponseEntity.ok().build(); // Swagger 설명용 API
    }
}