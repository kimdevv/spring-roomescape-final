package roomescape.reservation.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.reservation.business.dto.request.ReservationTimeCreateRequest;
import roomescape.reservation.exception.DuplicatedReservationTimeException;
import roomescape.reservation.exception.ReservationTimeDoesNotExistException;
import roomescape.reservation.model.ReservationTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약시간을_생성할_수_있다() {
        // Given
        ReservationTimeCreateRequest reservationTimeCreateRequest = new ReservationTimeCreateRequest(TestConstant.FUTURE_TIME);

        // When
        ReservationTime reservationTime = reservationTimeService.createReservationTime(reservationTimeCreateRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservationTime.getId()).isNotNull();
            softAssertions.assertThat(reservationTime.getStartAt()).isEqualTo(TestConstant.FUTURE_TIME);
        });
    }

    @Test
    void 이미_존재하는_시각에는_예약시간을_생성할_수_없다() {
        // Given
        reservationTimeService.createReservationTime(new ReservationTimeCreateRequest(TestConstant.FUTURE_TIME));

        // When & Then
        assertThatThrownBy(() -> reservationTimeService.createReservationTime(new ReservationTimeCreateRequest(TestConstant.FUTURE_TIME)))
                .isInstanceOf(DuplicatedReservationTimeException.class)
                .hasMessage("이미 해당 시각은 등록되어 있습니다.");
    }

    @Test
    void 저장되어_있는_모든_예약시간을_조회할_수_있다() {
        // Given
        ReservationTime reservationTime = reservationTimeService.createReservationTime(new ReservationTimeCreateRequest(TestConstant.FUTURE_TIME));;

        // When & Then
        assertThat(reservationTimeService.findAllReservationTimes()).containsExactlyInAnyOrder(reservationTime);
    }

    @Test
    void 저장되어_있는_예약시간을_id로_삭제할_수_있다() {
        // Given
        ReservationTime reservationTime = reservationTimeService.createReservationTime(new ReservationTimeCreateRequest(TestConstant.FUTURE_TIME));
        int originalCount = reservationTimeService.findAllReservationTimes().size();

        // When
        reservationTimeService.deleteReservationTimeById(reservationTime.getId());

        // Then
        assertThat(reservationTimeService.findAllReservationTimes().size()).isEqualTo(originalCount - 1);
    }

    @Test
    void 존재하지_않는_예약시간_id로는_예약시간을_삭제할_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTimeById(TestConstant.INVALID_ENTITY_ID))
                .isInstanceOf(ReservationTimeDoesNotExistException.class)
                .hasMessage("존재하지 않는 예약시간 id입니다.");
    }
}