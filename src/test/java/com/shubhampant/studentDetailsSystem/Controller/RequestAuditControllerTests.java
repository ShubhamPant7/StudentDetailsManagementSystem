package com.shubhampant.studentDetailsSystem.Controller;

import com.shubhampant.studentDetailsSystem.controller.RequestAuditController;
import com.shubhampant.studentDetailsSystem.entity.RequestAudit;
import com.shubhampant.studentDetailsSystem.service.RequestAuditService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestAuditControllerTests {

    @Mock
    private RequestAuditService requestAuditService;

    @InjectMocks
    private RequestAuditController requestAuditController;

    @Test
    void testGetAuditLogById() {

        RequestAudit audit = RequestAudit.builder().id(1L).username("admin").build();

        when(requestAuditService.getAuditById(1L)).thenReturn(audit);

        RequestAudit result = requestAuditController.getAuditLogById(1L);

        assertEquals(1L, result.getId());
        assertEquals("admin", result.getUsername());

        verify(requestAuditService).getAuditById(1L);
    }

    @Test
    void testGetAuditLogs() {

        RequestAudit audit = RequestAudit.builder()
                .id(1L)
                .username("admin")
                .build();

        Page<RequestAudit> page = new PageImpl<>(List.of(audit));

        when(requestAuditService.getAuditLogs(0, 20)).thenReturn(page);

        Page<RequestAudit> result = requestAuditController.getAuditLogs(0, 20);

        assertEquals(1, result.getContent().size());

        assertEquals("admin", result.getContent().get(0).getUsername());

        verify(requestAuditService).getAuditLogs(0, 20);
    }
}
