package roomescape.reservation.presentation.dto.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeCreateWebRequestTest {

    @Test
    void 시작시간은_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationTimeCreateWebRequest(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작시간은 null이 될 수 없습니다.");
    }
}