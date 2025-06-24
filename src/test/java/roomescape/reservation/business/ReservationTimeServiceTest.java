package roomescape.reservation.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.member.database.MemberRepository;
import roomescape.member.model.Member;
import roomescape.member.model.Role;
import roomescape.reservation.business.dto.request.ReservationTimeGetWithAvailabilityRequest;
import roomescape.reservation.database.ReservationRepository;
import roomescape.reservation.database.ThemeRepository;
import roomescape.reservation.exception.DuplicatedReservationTimeException;
import roomescape.reservation.exception.ReservationTimeDoesNotExistException;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationStatus;
import roomescape.reservation.model.ReservationTime;
import roomescape.reservation.model.Theme;
import roomescape.reservation.presentation.dto.request.ReservationTimeCreateWebRequest;
import roomescape.reservation.presentation.dto.response.ReservationTimeGetWithAvailabilityWebResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 예약시간을_생성할_수_있다() {
        // Given
        ReservationTimeCreateWebRequest reservationTimeCreateWebRequest = new ReservationTimeCreateWebRequest(TestConstant.FUTURE_TIME);

        // When
        ReservationTime reservationTime = reservationTimeService.createReservationTime(reservationTimeCreateWebRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservationTime.getId()).isNotNull();
            softAssertions.assertThat(reservationTime.getStartAt()).isEqualTo(TestConstant.FUTURE_TIME);
        });
    }

    @Test
    void 이미_존재하는_시각에는_예약시간을_생성할_수_없다() {
        // Given
        reservationTimeService.createReservationTime(new ReservationTimeCreateWebRequest(TestConstant.FUTURE_TIME));

        // When & Then
        assertThatThrownBy(() -> reservationTimeService.createReservationTime(new ReservationTimeCreateWebRequest(TestConstant.FUTURE_TIME)))
                .isInstanceOf(DuplicatedReservationTimeException.class)
                .hasMessage("이미 해당 시각은 등록되어 있습니다.");
    }

    @Test
    void 저장되어_있는_모든_예약시간을_조회할_수_있다() {
        // Given
        ReservationTime reservationTime = reservationTimeService.createReservationTime(new ReservationTimeCreateWebRequest(TestConstant.FUTURE_TIME));

        // When & Then
        assertThat(reservationTimeService.findAllReservationTimes()).containsExactlyInAnyOrder(reservationTime);
    }

    @Test
    void 저장되어_있는_모든_예약시간을_예약가능_여부와_함께_조회할_수_있다() {
        // Given
        ReservationTime reservationTime1 = reservationTimeService.createReservationTime(new ReservationTimeCreateWebRequest(TestConstant.FUTURE_TIME));
        ReservationTime reservationTime2 = reservationTimeService.createReservationTime(new ReservationTimeCreateWebRequest(TestConstant.FUTURE_TIME.plusMinutes(5)));
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        reservationRepository.save(new Reservation(member, TestConstant.FUTURE_DATE, reservationTime1, theme, ReservationStatus.RESERVED));
        ReservationTimeGetWithAvailabilityRequest reservationTimeGetWithAvailabilityRequest = new ReservationTimeGetWithAvailabilityRequest(theme.getId(), TestConstant.FUTURE_DATE);

        // When & Then
        assertThat(reservationTimeService.findAllReservationTimesWithAvailability(reservationTimeGetWithAvailabilityRequest))
                .containsExactlyInAnyOrder(new ReservationTimeGetWithAvailabilityWebResponse(reservationTime1.getId(), reservationTime1.getStartAt(), false),
                        new ReservationTimeGetWithAvailabilityWebResponse(reservationTime2.getId(), reservationTime2.getStartAt(), true));
    }

    @Test
    void 저장되어_있는_예약시간을_id로_삭제할_수_있다() {
        // Given
        ReservationTime reservationTime = reservationTimeService.createReservationTime(new ReservationTimeCreateWebRequest(TestConstant.FUTURE_TIME));
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