package roomescape.payment.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.payment.business.dto.request.PaymentApplyRequest;
import roomescape.payment.business.dto.response.PaymentApproveResponse;
import roomescape.payment.model.Payment;
import roomescape.payment.model.ProductType;
import roomescape.reservation.exception.ReservationDoesNotExistException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @MockitoBean
    private PaymentRestClientManager paymentRestClientManager;

    @BeforeEach
    void setUp() {
        when(paymentRestClientManager.apply(new PaymentApplyRequest(TestConstant.PAYMENT_AMOUNT, TestConstant.ORDER_ID, TestConstant.PAYMENT_KEY, ProductType.RESERVATION, 1L)))
                .thenReturn(new PaymentApproveResponse(TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT));
    }

    @Test
    void 결제_승인_요청을_보내고_성공적으로_DB에_저장할_수_있다() {
        // Given
        Long amount = TestConstant.PAYMENT_AMOUNT;
        String orderId = TestConstant.ORDER_ID;
        String paymentKey = TestConstant.PAYMENT_KEY;
        ProductType productType = ProductType.RESERVATION;
        Long productId = 1L;

        // When
        Payment payment = paymentService.applyPayment(new PaymentApplyRequest(amount, orderId, paymentKey, productType, productId));

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(payment.getId()).isNotNull();
            softAssertions.assertThat(payment.getAmount()).isEqualTo(amount);
            softAssertions.assertThat(payment.getOrderId()).isEqualTo(orderId);
            softAssertions.assertThat(payment.getPaymentKey()).isEqualTo(paymentKey);
            softAssertions.assertThat(payment.getProductType()).isEqualTo(productType);
            softAssertions.assertThat(payment.getProductId()).isEqualTo(productId);
        });
    }

    @Test
    void 예약_id를_통해_결제_레코드를_조회할_수_있다() {
        // Given
        Long reservationId = 1L;
        Payment payment = paymentService.applyPayment(new PaymentApplyRequest(TestConstant.PAYMENT_AMOUNT, TestConstant.ORDER_ID, TestConstant.PAYMENT_KEY, ProductType.RESERVATION, reservationId));

        // When & Then
        assertThat(paymentService.findPaymentByReservationId(reservationId)).isEqualTo(payment);
    }

    @Test
    void 잘못된_예약_id로는_결제_레코드를_조회할_수_없다() {
        // Given
        paymentService.applyPayment(new PaymentApplyRequest(TestConstant.PAYMENT_AMOUNT, TestConstant.ORDER_ID, TestConstant.PAYMENT_KEY, ProductType.RESERVATION, 1L));

        // When & Then
        assertThatThrownBy(() -> paymentService.findPaymentByReservationId(TestConstant.INVALID_ENTITY_ID))
                .isInstanceOf(ReservationDoesNotExistException.class)
                .hasMessage("존재하지 않는 예약 id입니다.");
    }
}