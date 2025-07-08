package com.project.nyang.global.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

 import io.swagger.v3.oas.models.OpenAPI;
 import io.swagger.v3.oas.models.info.Info;
 import io.swagger.v3.oas.models.security.SecurityRequirement;
 import io.swagger.v3.oas.models.security.SecurityScheme;
 import org.springdoc.core.customizers.OpenApiCustomizer;
 import org.springdoc.core.models.GroupedOpenApi;

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

    // ✅ 사용자 API 그룹
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("👤 사용자 API")
                .pathsToMatch("/api/v1/users/**")
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

    // ✅ 채팅 API 그룹
    @Bean
    public GroupedOpenApi chatApi() {
        return GroupedOpenApi.builder()
                .group("💬 채팅 API")
                .pathsToMatch("/api/v1/chat/**")
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