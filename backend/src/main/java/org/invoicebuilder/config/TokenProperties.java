package org.invoicebuilder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "spring.security.tokens")
public class TokenProperties {
    
    private Duration refreshTokenTtl = Duration.ofDays(7);
    private Duration accessTokenTtl = Duration.ofMinutes(15);
}
