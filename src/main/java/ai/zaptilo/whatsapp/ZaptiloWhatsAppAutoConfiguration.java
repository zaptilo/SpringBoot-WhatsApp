package ai.zaptilo.whatsapp;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Auto-configuration for the Zaptilo WhatsApp Spring Boot starter.
 * Provides a default {@link RestTemplate} bean if none is present in the application context.
 */
@Configuration
public class ZaptiloWhatsAppAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate zaptiloRestTemplate() {
        return new RestTemplate();
    }
}
