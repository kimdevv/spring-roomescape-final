package roomescape.payment.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;
import roomescape.payment.business.dto.request.PaymentApplyRequest;
import roomescape.payment.business.dto.response.PaymentApproveResponse;
import roomescape.payment.exception.PaymentApplyException;
import roomescape.payment.model.ConfidentialApproveExceptionCode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class PaymentRestClientManager {

    public static final String APPLY_SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

    private final RestClient paymentRestClient;

    public PaymentRestClientManager(RestClient paymentRestClient) {
        this.paymentRestClient = paymentRestClient;
    }

    public PaymentApproveResponse apply(PaymentApplyRequest paymentApplyRequest) {
        return paymentRestClient.post()
                .uri("/v1/payments/confirm")
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((APPLY_SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8)))
                .body(paymentApplyRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new PaymentApplyException(parseErrorMessage(response), HttpStatus.BAD_REQUEST);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new PaymentApplyException(parseErrorMessage(response), HttpStatus.INTERNAL_SERVER_ERROR);
                })
                .body(PaymentApproveResponse.class);
    }

    private String parseErrorMessage(ClientHttpResponse response) throws IOException {
        String body = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode bodyJson = objectMapper.readTree(body);
        String code = bodyJson.path("code").asText();
        if (ConfidentialApproveExceptionCode.isConfidential(code)) {
            System.out.println(code);
            return "결제 도중 오류가 발생했습니다.";
        }
        return bodyJson.path("message").asText();
    }
}
