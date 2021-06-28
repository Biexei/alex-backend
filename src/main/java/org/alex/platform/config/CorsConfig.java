package org.alex.platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private static final String[] HEADER = {"GET", "POST", "PATCH", "HEAD", "PUT", "DELETE", "OPTIONS", "TRACE", "FETCH"};

    /**
     * CORS跨域解决方案
     *
     * @param registry SpringBoot
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods(HEADER)
                .allowCredentials(true)
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
