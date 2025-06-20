package roomescape.reservation.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.reservation.business.dto.request.ReservationCreateRequest;
import roomescape.reservation.database.ReservationTimeRepository;
import roomescape.reservation.exception.DuplicatedReservationException;
import roomescape.reservation.exception.ReservationDoesNotExistException;
import roomescape.reservation.exception.ReservationTimeDoesNotExistException;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    void 예약을_저장할_수_있다() {
        // Given
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, time.getId());

        // When
        Reservation createdReservation = reservationService.createReservation(reservationCreateRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(createdReservation.getId()).isNotNull();
            softAssertions.assertThat(createdReservation.getName()).isEqualTo(TestConstant.MEMBER_NAME);
            softAssertions.assertThat(createdReservation.getDate()).isEqualTo(TestConstant.FUTURE_DATE);
            softAssertions.assertThat(createdReservation.getTime()).isEqualTo(time);
        });
    }

    @Test
    void 이미_예약이_등록된_시각에_중복으로_예약을_등록할_수_없다() {
        // Given
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        reservationService.createReservation(new ReservationCreateRequest(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, time.getId()));

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(new ReservationCreateRequest(TestConstant.MEMBER_NAME2, TestConstant.FUTURE_DATE, time.getId())))
                .isInstanceOf(DuplicatedReservationException.class)
                .hasMessage("중복된 시각에는 예약할 수 없습니다.");
    }

    @Test
    void 존재하지_않는_예약시간에는_예약을_등록할_수_없다() {
        // Given
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, TestConstant.INVALID_ENTITY_ID);

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(reservationCreateRequest))
                .isInstanceOf(ReservationTimeDoesNotExistException.class)
                .hasMessage("존재하지 않는 예약시간 id입니다.");
    }

    @Test
    void 예약을_취소할_수_있다() {
        // Given
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Reservation createdReservation = reservationService.createReservation(new ReservationCreateRequest(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, time.getId()));
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
        assertThatThrownBy(() -> reservationService.cancelReservation(TestConstant.INVALID_ENTITY_ID))
                .isInstanceOf(ReservationDoesNotExistException.class)
                .hasMessage("존재하지 않는 예약 id입니다.");
    }

    @Test
    void 모든_예약을_조회할_수_있다() {
        // Given
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(TestConstant.MEMBER_NAME, TestConstant.FUTURE_DATE, time.getId()));

        // When & Then
        assertThat(reservationService.findAllReservations()).containsExactlyInAnyOrder(reservation);
    }
}
