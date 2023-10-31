package tsvetkoff.currencyrates.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfiguration {

    private final RestProperties restProperties;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(restProperties.getConnectTimeout())
                .setReadTimeout(restProperties.getReadTimeout())
                .build();
    }

}
