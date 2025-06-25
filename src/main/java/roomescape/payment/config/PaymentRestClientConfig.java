package roomescape.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PaymentRestClientConfig {

    @Bean
    public RestClient paymentRestClient(RestClient.Builder restClientBuilder) {
        return restClientBuilder
                .baseUrl("https://api.tosspayments.com")
                .build();
    }
}
