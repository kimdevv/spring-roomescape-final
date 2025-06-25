package roomescape.payment.business.dto.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentApplyRequestTest {

    @Test
    void 주문_금액은_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new PaymentApplyRequest(null, "orderId", "paymentKey"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 금액은 null이 될 수 없습니다.");
    }

    @Test
    void 주문_번호는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new PaymentApplyRequest(1000L, null, "paymentKey"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 번호는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 주문_번호는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new PaymentApplyRequest(1000L, "   ", "paymentKey"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 번호는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 결제_키는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new PaymentApplyRequest(1000L, "orderId", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 키는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 결제_키는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new PaymentApplyRequest(1000L, "orderId", "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 키는 빈 값이 될 수 없습니다.");
    }
}