package com.shubhampant.studentDetailsSystem.Interceptor;

import com.shubhampant.studentDetailsSystem.entity.RequestAudit;
import com.shubhampant.studentDetailsSystem.service.RequestAuditService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RequestAuditInterceptorTests {
    @Mock
    private RequestAuditService requestAuditService;

    @InjectMocks
    private RequestAuditInterceptor requestAuditInterceptor;

    @AfterEach
    void cleanup() {    SecurityContextHolder.clearContext();   }

    @Test
    void testAfterCompletionSuccess() {

        Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "password", Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setRequestURI("/students");

        request.setMethod("GET");

        MockHttpServletResponse response = new MockHttpServletResponse();

        response.setStatus(200);

        requestAuditInterceptor.afterCompletion(request, response, new Object(), null);

        ArgumentCaptor<RequestAudit> captor = ArgumentCaptor.forClass(RequestAudit.class);

        verify(requestAuditService).save(captor.capture());

        RequestAudit audit = captor.getValue();

        assertEquals("admin", audit.getUsername());
        assertEquals("/students", audit.getEndpoint());
        assertEquals("GET", audit.getHttpMethod());
        assertEquals("SUCCESS", audit.getStatus());
        assertEquals(200, audit.getStatusCode());
    }

    @Test
    void testAfterCompletionFailure() {

        Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "password");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setRequestURI("/students");

        request.setMethod("POST");

        MockHttpServletResponse response = new MockHttpServletResponse();

        response.setStatus(500);

        Exception exception = new RuntimeException("Database error");

        requestAuditInterceptor.afterCompletion(request, response, new Object(), exception);

        ArgumentCaptor<RequestAudit> captor = ArgumentCaptor.forClass(RequestAudit.class);

        verify(requestAuditService).save(captor.capture());

        RequestAudit audit = captor.getValue();

        assertEquals("FAILED", audit.getStatus());
        assertEquals(500, audit.getStatusCode());
        assertEquals("Database error", audit.getErrorMessage());
    }

    @Test
    void testAfterCompletionAnonymousUser() {

        SecurityContextHolder.clearContext();

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setRequestURI("/students");

        request.setMethod("GET");

        MockHttpServletResponse response = new MockHttpServletResponse();

        response.setStatus(200);

        requestAuditInterceptor.afterCompletion(request, response, new Object(), null);

        ArgumentCaptor<RequestAudit> captor = ArgumentCaptor.forClass(RequestAudit.class);

        verify(requestAuditService).save(captor.capture());

        RequestAudit audit = captor.getValue();

        assertEquals("ANONYMOUS", audit.getUsername());
    }
}
