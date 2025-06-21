package roomescape.reservation.business.dto.request;

import org.junit.jupiter.api.Test;
import roomescape.TestConstant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeGetWithAvailabilityRequestTest {

    @Test
    void 테마는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationTimeGetWithAvailabilityRequest(null, TestConstant.FUTURE_DATE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마는 null이 될 수 없습니다.");
    }

    @Test
    void 날짜와_시간은_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationTimeGetWithAvailabilityRequest(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜는 null이 될 수 없습니다.");
    }
}
