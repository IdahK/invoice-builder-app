package org.invoicebuilder.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.invoicebuilder.config.ApiVersionConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiVersionInterceptor implements HandlerInterceptor {
    
    private final ApiVersionConfig apiVersionConfig;
    private static final List<String> SUPPORTED_VERSIONS = List.of("v1");
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Extract version from various sources
        String version = extractVersion(request);
        
        // Validate version
        if (!SUPPORTED_VERSIONS.contains(version)) {
            log.warn("Unsupported API version requested: {}", version);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setHeader("X-Supported-Versions", String.join(",", SUPPORTED_VERSIONS));
            return false;
        }
        
        // Set version as request attribute
        request.setAttribute("apiVersion", version);
        
        // Add version headers
        response.setHeader("API-Version", version);
        response.setHeader("API-Supported-Versions", String.join(",", SUPPORTED_VERSIONS));
        
        log.debug("API version: {}", version);
        return true;
    }
    
    private String extractVersion(HttpServletRequest request) {
        // 1. Check header first
        String version = request.getHeader("API-Version");
        if (version != null && !version.isEmpty()) {
            return version;
        }
        
        // 2. Check request parameter
        version = request.getParameter("version");
        if (version != null && !version.isEmpty()) {
            return version;
        }
        
        // 3. Extract from URI path
        String uri = request.getRequestURI();
        if (uri.contains("/api/v")) {
            int startIndex = uri.indexOf("/api/v") + 5; // "/api/v" length
            int endIndex = uri.indexOf("/", startIndex);
            if (endIndex == -1) {
                endIndex = uri.length();
            }
            version = uri.substring(startIndex, endIndex);
            if (SUPPORTED_VERSIONS.contains(version)) {
                return version;
            }
        }
        
        // 4. Return default version
        return apiVersionConfig.getDefaultVersion();
    }
}
