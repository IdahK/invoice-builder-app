package org.invoicebuilder.util;

import org.invoicebuilder.exception.UnsupportedApiVersionException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class ApiVersionUtils {
    
    public static final List<String> SUPPORTED_VERSIONS = Arrays.asList("v1");
    public static final String DEFAULT_VERSION = "v1";
    
    /**
     * Gets the current API version from the request context
     * @return Current API version
     */
    public static String getCurrentVersion() {
        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            String version = (String) request.getAttribute("apiVersion");
            return version != null ? version : DEFAULT_VERSION;
        }
        return DEFAULT_VERSION;
    }
    
    /**
     * Checks if the given version is supported
     * @param version Version to check
     * @return true if supported, false otherwise
     */
    public static boolean isVersionSupported(String version) {
        return SUPPORTED_VERSIONS.contains(version);
    }
    
    /**
     * Gets the current HttpServletRequest
     * @return Current request or null if not available
     */
    private static HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Validates version and throws exception if not supported
     * @param version Version to validate
     * @throws UnsupportedApiVersionException if version is not supported
     */
    public static void validateVersion(String version) {
        if (!isVersionSupported(version)) {
            throw new UnsupportedApiVersionException(version, String.join(",", SUPPORTED_VERSIONS));
        }
    }
}
