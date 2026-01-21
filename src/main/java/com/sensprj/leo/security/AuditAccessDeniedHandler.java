package com.sensprj.leo.security;

import com.sensprj.leo.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuditAccessDeniedHandler implements AccessDeniedHandler {

    private final AuditLogService auditLogService;

    public AuditAccessDeniedHandler(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null) ? auth.getName() : "anonymous";

        // nur für Assessment-Update-Attempts loggen (optional, aber passt zu deinem Scenario)
        if (request.getRequestURI().startsWith("/api/assessments")
                && ("PUT".equals(request.getMethod()) || "PATCH".equals(request.getMethod()))) {
            auditLogService.logPermissionRejected(username, "ASSESSMENT_UPDATE_ATTEMPT", "REJECTED_PERMISSIONS");
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Access denied - insufficient permissions\"}");
    }
}
