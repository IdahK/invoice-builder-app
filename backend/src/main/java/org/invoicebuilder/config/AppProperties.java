package org.invoicebuilder.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for application settings.
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    
    private Frontend frontend;
    private Roles roles;

    @Getter
    @Setter
    public static class Frontend {
        private String url;
    }

    @Getter
    @Setter
    public static class Roles {
        private DefaultRoles defaultRoles;

        @Getter
        @Setter
        public static class DefaultRoles {
            private String owner;
            private String admin;
            private String user;

        }
    }
}
