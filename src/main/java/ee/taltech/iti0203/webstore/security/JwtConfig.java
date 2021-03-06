package ee.taltech.iti0203.webstore.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * jwt configuration
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {
    private String secret;
    private int durationMin;

    public int getDurationMs() {
        return durationMin * 60 * 1000;
    }
}
