package com.shubhampant.studentDetailsSystem.service;

import com.shubhampant.studentDetailsSystem.entity.RequestAudit;
import com.shubhampant.studentDetailsSystem.repository.RequestAuditRepository;
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
}
