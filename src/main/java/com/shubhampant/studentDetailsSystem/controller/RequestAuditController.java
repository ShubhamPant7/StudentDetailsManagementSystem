package com.shubhampant.studentDetailsSystem.controller;


import com.shubhampant.studentDetailsSystem.entity.RequestAudit;
import com.shubhampant.studentDetailsSystem.service.RequestAuditService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audit")
public class RequestAuditController {

    private final RequestAuditService requestAuditService;

    public RequestAuditController(RequestAuditService requestAuditService) {
        this.requestAuditService = requestAuditService;
    }

    @GetMapping
    public Page<RequestAudit> getAuditLogs(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return requestAuditService.getAuditLogs(page, size);
    }

    @GetMapping("/{id}")
    public RequestAudit getAuditLogById(@PathVariable Long id) {
        return requestAuditService.getAuditById(id);
    }
}
