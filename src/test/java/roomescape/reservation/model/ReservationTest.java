package roomescape.reservation.model;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import roomescape.TestConstant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    void id가_없는_객체를_id를_가진_엔티티로_변환할_수_있다() {
        // Given
        Long id = 1L;
        Reservation reservation = new Reservation(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME);

        // When
        Reservation reservationEntity = reservation.toEntity(id);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservationEntity).isNotNull();
            softAssertions.assertThat(reservationEntity.getId()).isEqualTo(id);
            softAssertions.assertThat(reservationEntity.getName()).isEqualTo(TestConstant.MEMBER_NAME);
            softAssertions.assertThat(reservationEntity.getDate()).isEqualTo(TestConstant.FUTURE_DATE);
            softAssertions.assertThat(reservationEntity.getTime()).isEqualTo(TestConstant.FUTURE_TIME);
        });
    }

    @Test
    void id는_null이_될_수_없다() {
        // Given
        Reservation reservation = new Reservation(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME);

        // When & Then
        assertThatThrownBy(() -> reservation.toEntity(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id값은 null이 될 수 없습니다.");
    }
}
