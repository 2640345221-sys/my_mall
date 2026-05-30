package my_mall.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String TOKEN_HEADER = "token";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My Mall 接口文档")
                        .version("1.0")
                        .description("My Mall 商城系统 API 接口文档")
                        .contact(new Contact()
                                .name("刘一")
                                .email("helin242348@163.com")))
                .addSecurityItem(new SecurityRequirement().addList(TOKEN_HEADER))
                .components(new Components()
                        .addSecuritySchemes(TOKEN_HEADER, new SecurityScheme()
                                .name(TOKEN_HEADER)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("先登录获取 token，粘贴到这里")));
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户端")
                .pathsToMatch("/api/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("管理端")
                .pathsToMatch("/api/admin/**")
                .build();
    }
}
