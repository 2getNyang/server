package com.project.nyang.global.security.oauth2.docs;

import com.project.nyang.global.security.oauth2.OAuth2WithdrawNaverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@RequiredArgsConstructor
public class OAuth2DocController {

    private final OAuth2WithdrawNaverService oauth2WithdrawNaverService;

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
            - GET `{호스트주소}/logout`
            - 예시: `http://localhost:8080/logout`

            ✅ 참고 사항:
            - 로그아웃 시 자동으로 쿠키가 삭제됩니다.
            """)
    @GetMapping("/logout")
    public ResponseEntity<Void> logoutDoc() {
        return ResponseEntity.ok().build(); // Swagger 설명용 API
    }


    @Operation(summary = "구글 로그인", description = """
            구글 로그인은 아래 URL로 이동하면 로그인-회원가입 처리가 됩니다.
            `http://localhost:8080/oauth2/authorization/google`
            """)
    @GetMapping("/authorization/google")
    public ResponseEntity<Void> googleLoginDoc() {
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "네이버 회원탈퇴", description = """
            URL 로 이동시 회원탈퇴됩니다. cookie 에다가 sns access token 넣어주셔야 됩니다 !
            `http://localhost:8080/oauth2/docs/withdraw/naver`
            """)
    @GetMapping("/withdraw/naver")
    public ResponseEntity<Void> naverWithdrawDoc(
            @CookieValue("sns_access_token") String snsAccessToken
    ) {
        oauth2WithdrawNaverService.unlinkNaver(snsAccessToken);
        return ResponseEntity.ok().build();
    }


}