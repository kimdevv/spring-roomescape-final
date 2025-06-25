package roomescape.payment.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import roomescape.TestConstant;
import roomescape.payment.business.dto.request.PaymentApplyRequest;
import roomescape.payment.business.dto.response.PaymentApproveResponse;
import roomescape.payment.config.PaymentRestClientConfig;
import roomescape.payment.exception.PaymentApplyException;
import roomescape.payment.model.ProductType;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(PaymentRestClientManager.class)
@Import(PaymentRestClientConfig.class)
class PaymentRestClientManagerTest {

    @Autowired
    private PaymentRestClientManager paymentRestClientManager;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Test
    void 토스_서버로의_결제_승인_요청을_성공적으로_보낼_수_있다() {
        // Given
        String paymentKey = TestConstant.PAYMENT_KEY;
        String orderId = TestConstant.ORDER_ID;
        Long paymentAmount = TestConstant.PAYMENT_AMOUNT;
        mockRestServiceServer.expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
                .andRespond(withSuccess(String.format("""
                            {
                              "paymentKey": "%s",
                              "orderId": "%s",
                              "totalAmount": "%d"
                            }
                        """, paymentKey, orderId, paymentAmount), MediaType.APPLICATION_JSON));

        // When
        PaymentApproveResponse paymentApproveResponse = paymentRestClientManager.apply(new PaymentApplyRequest(paymentAmount, orderId, paymentKey, ProductType.RESERVATION, 1L));

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(paymentApproveResponse.paymentKey()).isEqualTo(paymentKey);
            softAssertions.assertThat(paymentApproveResponse.orderId()).isEqualTo(orderId);
            softAssertions.assertThat(paymentApproveResponse.totalAmount()).isEqualTo(paymentAmount);
        });
    }

    @Test
    void _400번대_오류가_뜨면_예외_객체를_던진다() {
        // Given
        String paymentKey = TestConstant.PAYMENT_KEY;
        String orderId = TestConstant.ORDER_ID;
        Long paymentAmount = TestConstant.PAYMENT_AMOUNT;
        mockRestServiceServer.expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
                .andRespond(withBadRequest().body("""
                            {
                              "code": "NOT_FOUND_PAYMENT",
                              "message": "존재하지 않는 결제 입니다."
                            }
                        """));

        // When & Then
        assertThatThrownBy(() -> paymentRestClientManager.apply(new PaymentApplyRequest(paymentAmount, orderId, paymentKey, ProductType.RESERVATION, 1L)))
                .isInstanceOf(PaymentApplyException.class)
                .hasMessage("존재하지 않는 결제 입니다.");
    }

    @Test
    void _500번대_오류가_뜨면_예외_객체를_던진다() {
        // Given
        String paymentKey = TestConstant.PAYMENT_KEY;
        String orderId = TestConstant.ORDER_ID;
        Long paymentAmount = TestConstant.PAYMENT_AMOUNT;
        mockRestServiceServer.expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
                .andRespond(withServerError().body("""
                            {
                              "code": "UNKNOWN_PAYMENT_ERROR",
                              "message": "결제에 실패했어요. 같은 문제가 반복된다면 은행이나 카드사로 문의해주세요."
                            }
                        """));

        // When & Then
        assertThatThrownBy(() -> paymentRestClientManager.apply(new PaymentApplyRequest(paymentAmount, orderId, paymentKey, ProductType.RESERVATION, 1L)))
                .isInstanceOf(PaymentApplyException.class)
                .hasMessage("결제에 실패했어요. 같은 문제가 반복된다면 은행이나 카드사로 문의해주세요.");
    }

    @Test
    void 일부_예외는_발생_시_예외_메세지를_숨겨서_던진다() {
        // Given
        String paymentKey = TestConstant.PAYMENT_KEY;
        String orderId = TestConstant.ORDER_ID;
        Long paymentAmount = TestConstant.PAYMENT_AMOUNT;
        mockRestServiceServer.expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
                .andRespond(withBadRequest().body("""
                            {
                              "code": "INVALID_API_KEY",
                              "message": "잘못된 시크릿키 연동 정보 입니다."
                            }
                        """));

        // When & Then
        assertThatThrownBy(() -> paymentRestClientManager.apply(new PaymentApplyRequest(paymentAmount, orderId, paymentKey, ProductType.RESERVATION, 1L)))
                .isInstanceOf(PaymentApplyException.class)
                .hasMessage("결제 도중 오류가 발생했습니다.");
    }
}