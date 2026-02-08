package org.invoicebuilder.exception;

import lombok.Getter;

@Getter
public class UnsupportedApiVersionException extends RuntimeException {
    private final String requestedVersion;
    private final String supportedVersions;

    public UnsupportedApiVersionException(String requestedVersion, String supportedVersions) {
        super(String.format("API version '%s' is not supported. Supported versions: %s", requestedVersion, supportedVersions));
        this.requestedVersion = requestedVersion;
        this.supportedVersions = supportedVersions;
    }
}
