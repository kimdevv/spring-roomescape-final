package roomescape.reservation.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.member.database.MemberDoesNotExistException;
import roomescape.member.database.MemberRepository;
import roomescape.member.model.Member;
import roomescape.member.model.Role;
import roomescape.reservation.business.dto.request.ReservationCreateRequest;
import roomescape.reservation.database.ReservationTimeRepository;
import roomescape.reservation.database.ThemeRepository;
import roomescape.reservation.exception.DuplicatedReservationException;
import roomescape.reservation.exception.ReservationDoesNotExistException;
import roomescape.reservation.exception.ReservationTimeDoesNotExistException;
import roomescape.reservation.exception.ThemeDoesNotExistException;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationStatus;
import roomescape.reservation.model.ReservationTime;
import roomescape.reservation.model.Theme;
import roomescape.reservation.presentation.dto.response.ReservationMineGetWebResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void 예약을_저장할_수_있다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED);

        // When
        Reservation createdReservation = reservationService.createReservation(reservationCreateRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(createdReservation.getId()).isNotNull();
            softAssertions.assertThat(createdReservation.getMember()).isEqualTo(member);
            softAssertions.assertThat(createdReservation.getDate()).isEqualTo(TestConstant.FUTURE_DATE);
            softAssertions.assertThat(createdReservation.getTime()).isEqualTo(time);
            softAssertions.assertThat(createdReservation.getTheme()).isEqualTo(theme);
            softAssertions.assertThat(createdReservation.getStatus()).isEqualTo(ReservationStatus.RESERVED);
        });
    }

    @Test
    void 대기_상태의_예약을_저장할_수_있다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.WAITING);

        // When
        Reservation createdReservation = reservationService.createReservation(reservationCreateRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(createdReservation.getId()).isNotNull();
            softAssertions.assertThat(createdReservation.getMember()).isEqualTo(member);
            softAssertions.assertThat(createdReservation.getDate()).isEqualTo(TestConstant.FUTURE_DATE);
            softAssertions.assertThat(createdReservation.getTime()).isEqualTo(time);
            softAssertions.assertThat(createdReservation.getTheme()).isEqualTo(theme);
            softAssertions.assertThat(createdReservation.getStatus()).isEqualTo(ReservationStatus.WAITING);
        });
    }

    @Test
    void 이미_예약이_등록된_시각에_중복으로_예약을_등록할_수_없다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED));

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED)))
                .isInstanceOf(DuplicatedReservationException.class)
                .hasMessage("이미 예약되어 있는 시각에는 예약할 수 없습니다.");
    }

    @Test
    void 존재하지_않는_멤버의_이메일으로는_예약을_등록할_수_없다() {
        // Given
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest("invalid@email.com", TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED);

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(reservationCreateRequest))
                .isInstanceOf(MemberDoesNotExistException.class)
                .hasMessage("잘못된 멤버의 요청입니다.");
    }

    @Test
    void 존재하지_않는_예약시간에는_예약을_등록할_수_없다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, TestConstant.INVALID_ENTITY_ID, theme.getId(), ReservationStatus.RESERVED);

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(reservationCreateRequest))
                .isInstanceOf(ReservationTimeDoesNotExistException.class)
                .hasMessage("존재하지 않는 예약시간 id입니다.");
    }

    @Test
    void 존재하지_않는_테마에는_예약을_등록할_수_없다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), TestConstant.INVALID_ENTITY_ID, ReservationStatus.RESERVED);

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(reservationCreateRequest))
                .isInstanceOf(ThemeDoesNotExistException.class)
                .hasMessage("존재하지 않는 테마 id입니다.");
    }

    @Test
    void 모든_예약을_조회할_수_있다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED));

        // When & Then
        assertThat(reservationService.findReservations(null)).containsExactlyInAnyOrder(reservation);
    }

    @Test
    void 특정_상태를_가진_예약만_조회할_수_있다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED));
        Reservation waitingReservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.WAITING));

        // When & Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservationService.findReservations(ReservationStatus.RESERVED)).containsExactlyInAnyOrder(reservation);
            softAssertions.assertThat(reservationService.findReservations(ReservationStatus.WAITING)).containsExactlyInAnyOrder(waitingReservation);
        });
    }

    @Test
    void 특정_멤버의_예약을_이메일로_모두_조회할_수_있다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED));
        Reservation waitingReservation1 = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.WAITING));
        Reservation waitingReservation2 = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.WAITING));
        Reservation waitingReservation3 = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.WAITING));
        List<ReservationMineGetWebResponse> expected = List.of(
                new ReservationMineGetWebResponse(reservation.getId(), reservation.getDate(), reservation.getTime().getStartAt(), reservation.getTheme().getName(), "예약"),
                new ReservationMineGetWebResponse(waitingReservation1.getId(), waitingReservation1.getDate(), waitingReservation1.getTime().getStartAt(), waitingReservation1.getTheme().getName(), "1번째 대기"),
                new ReservationMineGetWebResponse(waitingReservation2.getId(), waitingReservation2.getDate(), waitingReservation2.getTime().getStartAt(), waitingReservation2.getTheme().getName(), "2번째 대기"),
                new ReservationMineGetWebResponse(waitingReservation3.getId(), waitingReservation3.getDate(), waitingReservation3.getTime().getStartAt(), waitingReservation3.getTheme().getName(), "3번째 대기")
        );

        // When & Then
        assertThat(reservationService.findMyReservations(member.getEmail())).isEqualTo(expected);
    }

    @Test
    void 예약을_멤버로_필터링해서_조회할_수_있다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        Member member2 = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL2, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME2, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED));
        reservationService.createReservation(new ReservationCreateRequest(member2.getEmail(), TestConstant.FUTURE_DATE.plusDays(1), time.getId(), theme.getId(), ReservationStatus.RESERVED));

        // When & Then
        assertThat(reservationService.findFilteredReservations(member.getId(), null, null, null)).containsExactlyInAnyOrder(reservation);
    }

    @Test
    void 예약을_테마로_필터링해서_조회할_수_있다() {
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Theme theme2 = themeRepository.save(new Theme("다른 테마 이름", "다른 테마 설명", "다른 테마 썸네일 URL"));
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED));
        reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme2.getId(), ReservationStatus.RESERVED));

        // When & Then
        assertThat(reservationService.findFilteredReservations(null, theme.getId(), null, null)).containsExactlyInAnyOrder(reservation);
    }

    @Test
    void 예약을_날짜로_필터링해서_조회할_수_있다() {
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Reservation reservation1 = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED));
        Reservation reservation2 = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE.plusDays(1), time.getId(), theme.getId(), ReservationStatus.RESERVED));
        Reservation reservation3 = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE.plusDays(2), time.getId(), theme.getId(), ReservationStatus.RESERVED));
        reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE.plusDays(3), time.getId(), theme.getId(), ReservationStatus.RESERVED));
        reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE.plusDays(4), time.getId(), theme.getId(), ReservationStatus.RESERVED));

        // When & Then
        assertThat(reservationService.findFilteredReservations(null, null, TestConstant.FUTURE_DATE, TestConstant.FUTURE_DATE.plusDays(2))).containsExactlyInAnyOrder(reservation1, reservation2, reservation3);
    }

    @Test
    void 예약을_여러_조건으로_필터링해서_조회할_수_있다() {
        // Given
        Member member1 = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        Member member2 = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL2, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME2, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme1 = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Theme theme2 = themeRepository.save(new Theme("다른 테마 이름", "다른 테마 설명", "다른 테마 썸네일 URL"));
        reservationService.createReservation(new ReservationCreateRequest(member1.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme1.getId(), ReservationStatus.RESERVED));
        reservationService.createReservation(new ReservationCreateRequest(member2.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme2.getId(), ReservationStatus.RESERVED));
        reservationService.createReservation(new ReservationCreateRequest(member1.getEmail(), TestConstant.FUTURE_DATE.plusDays(1), time.getId(), theme2.getId(), ReservationStatus.RESERVED));
        reservationService.createReservation(new ReservationCreateRequest(member2.getEmail(), TestConstant.FUTURE_DATE.plusDays(1), time.getId(), theme1.getId(), ReservationStatus.RESERVED));
        reservationService.createReservation(new ReservationCreateRequest(member1.getEmail(), TestConstant.FUTURE_DATE.plusDays(2), time.getId(), theme1.getId(), ReservationStatus.RESERVED));
        reservationService.createReservation(new ReservationCreateRequest(member2.getEmail(), TestConstant.FUTURE_DATE.plusDays(2), time.getId(), theme2.getId(), ReservationStatus.RESERVED));
        reservationService.createReservation(new ReservationCreateRequest(member1.getEmail(), TestConstant.FUTURE_DATE.plusDays(3), time.getId(), theme2.getId(), ReservationStatus.RESERVED));
        reservationService.createReservation(new ReservationCreateRequest(member2.getEmail(), TestConstant.FUTURE_DATE.plusDays(3), time.getId(), theme1.getId(), ReservationStatus.RESERVED));

        // When & Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservationService.findFilteredReservations(member1.getId(), theme2.getId(), null, null)).hasSize(2);
            softAssertions.assertThat(reservationService.findFilteredReservations(member2.getId(), null, TestConstant.FUTURE_DATE.plusDays(1), TestConstant.FUTURE_DATE.plusDays(3))).hasSize(3);
            softAssertions.assertThat(reservationService.findFilteredReservations(member2.getId(), theme1.getId(), TestConstant.FUTURE_DATE.plusDays(1), TestConstant.FUTURE_DATE.plusDays(3))).hasSize(2);
        });
    }

    @Test
    void 예약을_취소할_수_있다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Reservation createdReservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED));
        int originalCount = reservationService.findReservations(null).size();

        // When
        reservationService.cancelReservation(createdReservation.getId());

        // Then
        assertThat(reservationService.findReservations(null).size()).isEqualTo(originalCount - 1);
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
}
