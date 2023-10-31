package tsvetkoff.currencyrates.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "data.rest")
public class RestProperties {
    private Duration connectTimeout;
    private Duration readTimeout;
}
