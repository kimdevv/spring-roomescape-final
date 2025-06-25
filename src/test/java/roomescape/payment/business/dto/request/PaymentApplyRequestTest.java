package roomescape.payment.business.dto.request;

import org.junit.jupiter.api.Test;
import roomescape.payment.model.ProductType;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentApplyRequestTest {

    @Test
    void 주문_금액은_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new PaymentApplyRequest(null, "orderId", "paymentKey", ProductType.RESERVATION, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 금액은 null이 될 수 없습니다.");
    }

    @Test
    void 주문_번호는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new PaymentApplyRequest(1000L, null, "paymentKey", ProductType.RESERVATION, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 번호는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 주문_번호는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new PaymentApplyRequest(1000L, "   ", "paymentKey", ProductType.RESERVATION, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 번호는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 결제_키는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new PaymentApplyRequest(1000L, "orderId", null, ProductType.RESERVATION, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 키는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 결제_키는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new PaymentApplyRequest(1000L, "orderId", "   ", ProductType.RESERVATION, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 키는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 상품_종류는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new PaymentApplyRequest(1000L, "orderId", "paymentKey", null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 종류는 null이 될 수 없습니다.");
    }

    @Test
    void 상품_번호는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new PaymentApplyRequest(1000L, "orderId", "paymentKey", ProductType.RESERVATION, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 id는 null이 될 수 없습니다.");
    }
}