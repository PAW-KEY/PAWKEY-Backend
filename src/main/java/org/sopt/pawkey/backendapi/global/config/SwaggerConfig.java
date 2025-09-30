package org.sopt.pawkey.backendapi.global.config;

import java.util.ArrayList;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	public SwaggerConfig(MappingJackson2HttpMessageConverter converter) {
		var supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
		supportedMediaTypes.add(new MediaType("application", "octet-stream"));
		converter.setSupportedMediaTypes(supportedMediaTypes);
	}

	@Bean
	public GroupedOpenApi publicApi() { // GroupedOpenApi 빈을 통해 API를 그룹화
		return GroupedOpenApi.builder()
			.group("springdoc-public")
			.pathsToMatch("/**") // 모든 경로를 문서화
			.build();
	}

	@Bean
	public OpenAPI customOpenAPI() { // OpenAPI 빈을 사용하여 API 문서의 정보(제목, 버전, 설명)를 커스터마이징
		return new OpenAPI()
			.addServersItem(new Server().url("/"))
			.info(new Info()
				.title("paw-key API")
				.version("v1")
				.description("paw-key API 명세서"))
				.components(new Components()
				.addSecuritySchemes("bearerAuth",
					new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")))
			.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
	}
}
