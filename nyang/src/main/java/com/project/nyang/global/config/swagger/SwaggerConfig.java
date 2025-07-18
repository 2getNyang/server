package com.project.nyang.global.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

 import io.swagger.v3.oas.models.OpenAPI;
 import io.swagger.v3.oas.models.info.Info;
 import io.swagger.v3.oas.models.security.SecurityRequirement;
 import io.swagger.v3.oas.models.security.SecurityScheme;
 import org.springdoc.core.customizers.OpenApiCustomizer;
 import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.web.bind.annotation.BindParam;

/**
 * Swagger관련 설정파일 입니다.
  * http://localhost:8080/swagger-ui/index.html
 *
 * @author : 선순주
 * @fileName : swaggerConfig
 * @since : 2025-07-07
 */
@Configuration
public class SwaggerConfig {

     // ✅ 소셜 로그인 API 그룹
     @Bean
     public GroupedOpenApi oauthApi() {
         return GroupedOpenApi.builder()
                 .group("🌐 소셜 로그인 API")
                 .pathsToMatch("/oauth2/docs/**")
                 .addOpenApiCustomizer(jwtSecurityCustomizer())
                 .build();
     }

     // ✅ 인증 API 그룹
     @Bean
     public GroupedOpenApi authApi() {
         return GroupedOpenApi.builder()
                 .group("🔐 인증 API")
                 .pathsToMatch("/api/auth/**")
                 .addOpenApiCustomizer(jwtSecurityCustomizer())
                 .build();
     }

     // ✅ 마이페이지 API 그룹
     @Bean
     public GroupedOpenApi myPageApi() {
         return GroupedOpenApi.builder()
                 .group("👤 마이페이지 API")
                 .pathsToMatch("/api/v1/my/**")
                 .addOpenApiCustomizer(jwtSecurityCustomizer())
                 .build();
     }

     // ✅ 보호소 API 그룹
     @Bean
     public GroupedOpenApi shelterApi() {
         return GroupedOpenApi.builder()
                 .group("🏠 보호소 API")
                 .pathsToMatch("/api/v1/shelters/**")
                 .addOpenApiCustomizer(jwtSecurityCustomizer())
                 .build();
     }

    // ✅ 사용자 API 그룹
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("👤 사용자 API")
                .pathsToMatch("/api/v1/user/**")
                .addOpenApiCustomizer(jwtSecurityCustomizer())
                .build();
    }

    // ✅ 게시판 API 그룹
    @Bean
    public GroupedOpenApi boardApi() {
        return GroupedOpenApi.builder()
                .group("📝 게시판 API")
                .pathsToMatch("/api/v1/boards/**")
                .addOpenApiCustomizer(jwtSecurityCustomizer())
                .build();
    }

    // ✅ 게시판 API 그룹
    @Bean
    public GroupedOpenApi commentApi() {
        return GroupedOpenApi.builder()
                .group("📝 댓글 API")
                .pathsToMatch("/api/v1/comments/**")
                .addOpenApiCustomizer(jwtSecurityCustomizer())
                .build();
    }

    // ✅ 좋아요/찜 API 그룹
    @Bean
    public GroupedOpenApi LikeItApi() {
        return GroupedOpenApi.builder()
                .group("⭐ 좋아요 API")
                .pathsToMatch("/api/v1/bookmark/**", "/api/v1/like/**")
                .addOpenApiCustomizer(jwtSecurityCustomizer())
                .build();
    }

    // ✅ 채팅 API 그룹
    @Bean
    public GroupedOpenApi chatApi() {
        return GroupedOpenApi.builder()
                .group("💬 채팅 API")
                .pathsToMatch("/api/v1/chat/**")
                .addOpenApiCustomizer(jwtSecurityCustomizer())
                .build();
    }

     // ✅ 동물 API 그룹
     @Bean
     public GroupedOpenApi animalApi() {
         return GroupedOpenApi.builder()
                 .group("🐱 동물 API")
                 .pathsToMatch("/api/v1/animals/**")
                 .addOpenApiCustomizer(jwtSecurityCustomizer())
                 .build();
     }

     // ✅ 이달의 동물 추천 API 그룹
     @Bean
     public GroupedOpenApi animalRecommendationApi() {
         return GroupedOpenApi.builder()
                 .group("😽 동물 추천 API")
                 .pathsToMatch("/api/v1/recommendations/**")
                 .addOpenApiCustomizer(jwtSecurityCustomizer())
                 .build();
     }

    // ✅ 입양 신청 API 그룹
    @Bean
    public GroupedOpenApi  AdoptionApi() {
        return GroupedOpenApi .builder()
                .group("📝 입양신청 API")
                .pathsToMatch("/api/v1/adoptions/**")
                .addOpenApiCustomizer(jwtSecurityCustomizer())
                .build();
    }

     // ✅ 알림 테스트 API 그룹
     @Bean
     public GroupedOpenApi  notipicationTestOpenApi(){
         return GroupedOpenApi .builder()
                 .group("🔔 알림 백엔드 테스트 API")
                 .pathsToMatch("/api/v1/notifications/test/**")
                 .addOpenApiCustomizer(jwtSecurityCustomizer())
                 .build();
     }

    // ✅ 알림  API 그룹
    @Bean
    public GroupedOpenApi  notipicationOpenApi(){
        return GroupedOpenApi .builder()
                .group("🔔 알림 API, 프론트 연동 아직 X")
                .pathsToMatch("/api/v1/notifications/**")
                .addOpenApiCustomizer(jwtSecurityCustomizer())
                .build();
    }

    // ✅ API 메타정보
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("함께하개냥 API")
                        .version("v1.0")
                        .description("함께하개냥 API 문서입니다."));
    }

     // ✅ 지역 API 그룹 추가
     @Bean
     public GroupedOpenApi regionApi() {
         return GroupedOpenApi.builder()
                 .group("🌍 지역 API")
                 .pathsToMatch("/api/v1/regions/**")
                 .addOpenApiCustomizer(jwtSecurityCustomizer())
                 .build();
     }


    // ✅ JWT 보안 설정 커스터마이저
    private OpenApiCustomizer jwtSecurityCustomizer() {
        return openApi -> openApi.addSecurityItem(new SecurityRequirement().addList("jwt token"))
                .getComponents()
                .addSecuritySchemes("jwt token", new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .in(SecurityScheme.In.HEADER)
                        .bearerFormat("JWT")
                        .scheme("bearer"));
    }
}