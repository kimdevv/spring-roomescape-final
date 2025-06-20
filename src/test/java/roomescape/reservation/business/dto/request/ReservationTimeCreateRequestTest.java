package roomescape.reservation.business.dto.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeCreateRequestTest {

    @Test
    void 시작시간은_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationTimeCreateRequest(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작시간은 null이 될 수 없습니다.");
    }
}