package com.shubhampant.studentDetailsSystem.Service;

import com.shubhampant.studentDetailsSystem.entity.RequestAudit;
import com.shubhampant.studentDetailsSystem.repository.RequestAuditRepository;
import com.shubhampant.studentDetailsSystem.service.RequestAuditService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestAuditServiceTests {
    @Mock
    private RequestAuditRepository requestAuditRepository;

    @InjectMocks
    private RequestAuditService requestAuditService;

    @Test
    void testSaveAudit() {

        RequestAudit audit = RequestAudit.builder().id(1L).username("admin").build();

        requestAuditService.save(audit);

        verify(requestAuditRepository).save(audit);
    }

    @Test
    void testGetAuditLogs() {

        RequestAudit audit = RequestAudit.builder().id(1L).username("admin").build();

        Page<RequestAudit> page = new PageImpl<>(List.of(audit));

        when(requestAuditRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<RequestAudit> result = requestAuditService.getAuditLogs(0, 10);

        assertEquals(1, result.getContent().size());

        verify(requestAuditRepository).findAll(PageRequest.of(0, 10));
    }

    @Test
    void testGetAuditByIdSuccess() {

        RequestAudit audit = RequestAudit.builder().id(1L).username("admin").build();

        when(requestAuditRepository.findById(1L)).thenReturn(Optional.of(audit));

        RequestAudit result = requestAuditService.getAuditById(1L);

        assertEquals(1L, result.getId());

        verify(requestAuditRepository).findById(1L);
    }

    @Test
    void testGetAuditByIdNotFound() {

        when(requestAuditRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> requestAuditService.getAuditById(1L));

        assertEquals("Audit record not found", exception.getMessage());

        verify(requestAuditRepository).findById(1L);
    }
}
