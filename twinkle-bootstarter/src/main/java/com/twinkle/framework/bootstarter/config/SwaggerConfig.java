package com.twinkle.framework.bootstarter.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-06-20 17:58<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@OpenAPIDefinition(
        servers = {
                @Server(description = "Dev Server", url = "http://localhost:8080"),
                @Server(description = "Test Server", url = "https://test.twinkle.com")
        }
)
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // 配置接口文档基本信息
                .info(this.getApiInfo());
    }

    private Info getApiInfo() {
        return new Info()
                // 配置文档标题
                .title("Twinkle Standard Service API.")
                // 配置文档描述
                .description("Twinkle Framework Standard Service with Business APIs.")
                // 配置作者信息
                .contact(new Contact().name("Chenxj").url("https://www.github.com").email("cx_hit@126.com"))
                // 配置License许可证信息
                .license(new License().name("Apache 2.0").url("https://www.github.com"))
                // 概述信息
                .summary("SpringBoot3 Swagger3")
                .termsOfService("https://www.github.com")
                // 配置版本号
                .version("2.0");
    }
}

