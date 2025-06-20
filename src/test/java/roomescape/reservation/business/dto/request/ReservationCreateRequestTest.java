package roomescape.reservation.business.dto.request;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import roomescape.TestConstant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationCreateRequestTest {

    @Test
    void 이름은_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationCreateRequest(null, TestConstant.FUTURE_DATE, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 null이 될 수 없습니다.");
    }

    @Test
    void 이름은_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new ReservationCreateRequest("   ", TestConstant.FUTURE_DATE, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 빈 값이 될 수 없습니다.");
    }

    @Test
    void 날짜와_시간은_null이_될_수_없다() {
        // Given
        // When
        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThatThrownBy(() -> new ReservationCreateRequest(TestConstant.MEMBER_NAME, null, 1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("날짜와 시간은 null이 될 수 없습니다.");
            assertThatThrownBy(() -> new ReservationCreateRequest(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("날짜와 시간은 null이 될 수 없습니다.");
        });
    }
}
