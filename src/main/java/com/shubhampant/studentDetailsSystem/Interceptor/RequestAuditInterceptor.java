package com.shubhampant.studentDetailsSystem.Interceptor;


import com.shubhampant.studentDetailsSystem.entity.RequestAudit;
import com.shubhampant.studentDetailsSystem.repository.RequestAuditRepository;
import com.shubhampant.studentDetailsSystem.service.RequestAuditService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestAuditInterceptor implements HandlerInterceptor {
    private final RequestAuditService requestAuditService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String username = "ANONYMOUS";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {

            username = authentication.getName();
        }

        RequestAudit audit = RequestAudit.builder()
                .username(username)
                .endpoint(request.getRequestURI())
                .httpMethod(request.getMethod())
                .status(response.getStatus() >= 400 ? "FAILED" : "SUCCESS")
                .statusCode(response.getStatus())
                .errorMessage(ex != null ? ex.getMessage() : null)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            requestAuditService.save(audit);
        } catch (Exception e) {
            log.error("Failed to persist audit record for endpoint={}", request.getRequestURI(), e);
        }
    }
}
