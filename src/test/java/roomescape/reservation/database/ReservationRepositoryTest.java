package roomescape.reservation.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.TestConstant;
import roomescape.reservation.model.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationRepositoryTest {

    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new MemoryReservationRepository(new ArrayList<>());
    }

    @Test
    void 예약을_저장한다() {
        // Given
        Reservation reservation = new Reservation("프리", TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME);

        // When & Then
        assertThatCode(() -> reservationRepository.save(reservation))
                .doesNotThrowAnyException();
    }

    @Test
    void 이미_저장된_예약_엔티티는_다시_저장할_수_없다() {
        // Given
        Reservation savedReservation = reservationRepository.save(new Reservation("프리", TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME));

        // When & Then
        assertThatThrownBy(() -> reservationRepository.save(savedReservation))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 DB에 저장되어 있는 엔티티입니다.");
    }

    @Test
    void 모든_예약_엔티티를_조회할_수_있다() {
        // Given
        Reservation savedReservation = reservationRepository.save(new Reservation("프리", TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME));
        Reservation savedReservation2 = reservationRepository.save(new Reservation("프리", TestConstant.FUTURE_DATE.plusDays(1), TestConstant.FUTURE_TIME));

        // When & Then
        assertThat(reservationRepository.findAll())
                .containsExactlyInAnyOrder(savedReservation, savedReservation2);
    }
}
