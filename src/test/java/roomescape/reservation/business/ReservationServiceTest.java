package roomescape.reservation.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.presentation.dto.request.ReservationCreateRequest;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void 예약을_저장할_수_있다() {
        // Given
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, TestConstant.FUTURE_TIME);

        // When
        Reservation createdReservation = reservationService.createReservation(reservationCreateRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(createdReservation.getId()).isEqualTo(1L);
            softAssertions.assertThat(createdReservation.getName()).isEqualTo(TestConstant.MEMBER_NAME);
            softAssertions.assertThat(createdReservation.getDate()).isEqualTo(TestConstant.FUTURE_DATE);
            softAssertions.assertThat(createdReservation.getTime()).isEqualTo(TestConstant.FUTURE_TIME);
        });
    }
}
