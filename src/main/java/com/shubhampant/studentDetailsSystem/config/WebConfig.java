package com.shubhampant.studentDetailsSystem.config;

import com.shubhampant.studentDetailsSystem.Interceptor.RequestAuditInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Spring MVC configuration, registers application wide interceptors that should be executed for incoming HTTP requests.
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RequestAuditInterceptor requestAuditInterceptor;

    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        registry.addInterceptor(requestAuditInterceptor);
    }
}
