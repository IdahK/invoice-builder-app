package org.invoicebuilder.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConfigurationProperties(prefix = "api")
@Getter
@Setter
public class ApiVersionConfig implements WebMvcConfigurer {
    
    private String version = "v1";
    private String defaultVersion = "v1";
    private boolean enableVersionHeader = true;
    private boolean enableVersionParameter = true;

    
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        if (enableVersionParameter) {
            configurer.favorParameter(true)
                    .parameterName("version")
                    .ignoreAcceptHeader(false);
        }
    }
}
