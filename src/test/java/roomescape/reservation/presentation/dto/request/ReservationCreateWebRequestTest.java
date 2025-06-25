package roomescape.reservation.presentation.dto.request;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import roomescape.TestConstant;
import roomescape.reservation.model.ReservationStatus;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationCreateWebRequestTest {

    @Test
    void 날짜와_시간은_null이_될_수_없다() {
        // Given
        // When
        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThatThrownBy(() -> new ReservationCreateWebRequest(null, 1L, 1L, ReservationStatus.RESERVED, "paymentKey", "orderId", 1000L, "NORMAL"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("날짜와 시간은 null이 될 수 없습니다.");
            assertThatThrownBy(() -> new ReservationCreateWebRequest(TestConstant.FUTURE_DATE, null, 1L, ReservationStatus.RESERVED, "paymentKey", "orderId", 1000L, "NORMAL"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("날짜와 시간은 null이 될 수 없습니다.");
        });
    }

    @Test
    void 테마는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationCreateWebRequest(TestConstant.FUTURE_DATE, 1L, null, ReservationStatus.RESERVED, "paymentKey", "orderId", 1000L, "NORMAL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마는 null이 될 수 없습니다.");
    }

    @Test
    void 예약상태는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationCreateWebRequest(TestConstant.FUTURE_DATE, 1L, 1L, null, "paymentKey", "orderId", 1000L, "NORMAL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약상태는 null이 될 수 없습니다.");
    }

    @Test
    void 결제_키는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationCreateWebRequest(TestConstant.FUTURE_DATE, 1L, 1L, ReservationStatus.RESERVED, null, "orderId", 1000L, "NORMAL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 키는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 결제_키는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationCreateWebRequest(TestConstant.FUTURE_DATE, 1L, 1L, ReservationStatus.RESERVED, "   ", "orderId", 1000L, "NORMAL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 키는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 주문_번호는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationCreateWebRequest(TestConstant.FUTURE_DATE, 1L, 1L, ReservationStatus.RESERVED, "paymentKey", null, 1000L, "NORMAL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 번호는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 주문_번호는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationCreateWebRequest(TestConstant.FUTURE_DATE, 1L, 1L, ReservationStatus.RESERVED, "paymentKey", "   ", 1000L, "NORMAL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 번호는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 주문_금액은_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationCreateWebRequest(TestConstant.FUTURE_DATE, 1L, 1L, ReservationStatus.RESERVED, "paymentKey", "orderId", null, "NORMAL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 금액은 null이 될 수 없습니다.");
    }

    @Test
    void 주문_종류는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationCreateWebRequest(TestConstant.FUTURE_DATE, 1L, 1L, ReservationStatus.RESERVED, "paymentKey", "orderId", 1000L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 종류는 null이 될 수 없습니다.");
    }

    @Test
    void 주문_종류는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationCreateWebRequest(TestConstant.FUTURE_DATE, 1L, 1L, ReservationStatus.RESERVED, "paymentKey", "orderId", 1000L, "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 종류는 null이 될 수 없습니다.");
    }
}
