package roomescape.reservation.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.reservation.exception.DuplicatedReservationException;
import roomescape.reservation.exception.ReservationDoesNotExistException;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.presentation.dto.request.ReservationCreateWebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void 예약을_저장할_수_있다() {
        // Given
        ReservationCreateWebRequest reservationCreateWebRequest = new ReservationCreateWebRequest(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME);

        // When
        Reservation createdReservation = reservationService.createReservation(reservationCreateWebRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(createdReservation.getId()).isEqualTo(1L);
            softAssertions.assertThat(createdReservation.getName()).isEqualTo(TestConstant.MEMBER_NAME);
            softAssertions.assertThat(createdReservation.getDate()).isEqualTo(TestConstant.FUTURE_DATE);
            softAssertions.assertThat(createdReservation.getTime()).isEqualTo(TestConstant.FUTURE_TIME);
        });
    }

    @Test
    void 이미_예약이_등록된_시각에_중복으로_예약을_등록할_수_없다() {
        // Given
        reservationService.createReservation(new ReservationCreateWebRequest(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME));

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(new ReservationCreateWebRequest(TestConstant.MEMBER_NAME2, TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME)))
                .isInstanceOf(DuplicatedReservationException.class)
                .hasMessage("중복된 시각에는 예약할 수 없습니다.");
    }

    @Test
    void 예약을_취소할_수_있다() {
        // Given
        ReservationCreateWebRequest reservationCreateWebRequest = new ReservationCreateWebRequest(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME);
        Reservation createdReservation = reservationService.createReservation(reservationCreateWebRequest);
        int originalCount = reservationService.findAllReservations().size();

        // When
        reservationService.cancelReservation(createdReservation.getId());

        // Then
        assertThat(reservationService.findAllReservations().size()).isEqualTo(originalCount - 1);
    }

    @Test
    void 존재하지_않는_예약의_id로는_예약을_취소할_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> reservationService.cancelReservation(Long.MAX_VALUE))
                .isInstanceOf(ReservationDoesNotExistException.class)
                .hasMessage("존재하지 않는 예약 id입니다.");
    }

    @Test
    void 모든_예약을_조회할_수_있다() {
        // Given
        Reservation reservation = reservationService.createReservation(new ReservationCreateWebRequest(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME));

        // When & Then
        assertThat(reservationService.findAllReservations()).containsExactlyInAnyOrder(reservation);
    }
}
