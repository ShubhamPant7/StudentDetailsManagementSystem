package com.shubhampant.studentDetailsSystem.service;

import com.shubhampant.studentDetailsSystem.entity.RequestAudit;
import com.shubhampant.studentDetailsSystem.repository.RequestAuditRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class RequestAuditService {

    private final RequestAuditRepository requestAuditRepository;

    public RequestAuditService(RequestAuditRepository requestAuditRepository) {
        this.requestAuditRepository = requestAuditRepository;
    }

    public void save(RequestAudit audit) {
        requestAuditRepository.save(audit);
    }

    public Page<RequestAudit> getAuditLogs(int page, int size) {
        return requestAuditRepository.findAll(PageRequest.of(page, size));
    }

    public RequestAudit getAuditById(Long id) {

        return requestAuditRepository.findById(id).orElseThrow(() -> new RuntimeException("Audit record not found"));
    }
}