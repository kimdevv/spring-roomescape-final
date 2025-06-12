package roomescape.reservation.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.TestConstant;
import roomescape.reservation.model.Reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void 예약을_저장한다() {
        // Given
        Reservation reservation = new Reservation(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME);

        // When & Then
        assertThatCode(() -> reservationRepository.save(reservation))
                .doesNotThrowAnyException();
    }

    @Test
    void 이미_저장된_예약_엔티티는_다시_저장할_수_없다() {
        // Given
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME));

        // When & Then
        assertThatThrownBy(() -> reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 모든_예약_엔티티를_조회할_수_있다() {
        // Given
        Reservation savedReservation = reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME));
        Reservation savedReservation2 = reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE.plusDays(1), TestConstant.FUTURE_TIME));

        // When & Then
        assertThat(reservationRepository.findAll())
                .containsExactlyInAnyOrder(savedReservation, savedReservation2);
    }

    @Test
    void 예약이_존재하는지_검사하고_존재한다면_true를_반환한다() {
        // Given
        Reservation reservation = reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME));

        // When & Then
        assertThat(reservationRepository.existsById(reservation.getId())).isTrue();
    }

    @Test
    void 예약이_존재하는지_검사하고_존재하지_않는다면_false를_반환한다() {
        // Given
        // When
        // Then
        assertThat(reservationRepository.existsById(Long.MAX_VALUE)).isFalse();
    }
}
