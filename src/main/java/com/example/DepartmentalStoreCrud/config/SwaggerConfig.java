package com.example.DepartmentalStoreCrud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.GroupedOpenApi;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Departmental Store API",
                version = "1.0",
                description = "API documentation for Departmental Store CRUD operations"
//                contact = @io.swagger.v3.oas.annotations.info.Contact(
//                        name = "Your Name",
//                        email = "your-email@example.com"
//                )
//                license = @io.swagger.v3.oas.annotations.info.License(
//                        name = "Apache 2.0",
//                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
//                )
        ),
        servers = {
                @Server(url = "http://localhost:9111", description = "Local Server")
        }
)
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
    }

    @Bean
    public GroupedOpenApi api() {
        String[] basePackages = {"com.example.DepartmentalStoreCrud.controller"};
        return GroupedOpenApi.builder()
                .group("api")
                .packagesToScan(basePackages)
                .build();
    }
}