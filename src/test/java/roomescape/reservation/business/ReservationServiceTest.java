package roomescape.reservation.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.member.database.MemberDoesNotExistException;
import roomescape.member.database.MemberRepository;
import roomescape.member.model.Member;
import roomescape.member.model.Role;
import roomescape.payment.business.PaymentRestClientManager;
import roomescape.payment.business.dto.request.PaymentApplyRequest;
import roomescape.payment.business.dto.response.PaymentApproveResponse;
import roomescape.payment.model.ProductType;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    @MockitoBean
    private PaymentRestClientManager paymentRestClientManager;

    @BeforeEach
    void setUp() {
        when(paymentRestClientManager.apply(new PaymentApplyRequest(TestConstant.PAYMENT_AMOUNT, TestConstant.ORDER_ID, TestConstant.PAYMENT_KEY, ProductType.RESERVATION, 1L)))
                .thenReturn(new PaymentApproveResponse(TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT));
        when(paymentRestClientManager.apply(new PaymentApplyRequest(TestConstant.PAYMENT_AMOUNT, TestConstant.ORDER_ID2, TestConstant.PAYMENT_KEY2, ProductType.RESERVATION, 2L)))
                .thenReturn(new PaymentApproveResponse(TestConstant.PAYMENT_KEY2, TestConstant.ORDER_ID2, TestConstant.PAYMENT_AMOUNT));
        when(paymentRestClientManager.apply(new PaymentApplyRequest(TestConstant.PAYMENT_AMOUNT, TestConstant.ORDER_ID3, TestConstant.PAYMENT_KEY3, ProductType.RESERVATION, 3L)))
                .thenReturn(new PaymentApproveResponse(TestConstant.PAYMENT_KEY3, TestConstant.ORDER_ID3, TestConstant.PAYMENT_AMOUNT));
        when(paymentRestClientManager.apply(new PaymentApplyRequest(TestConstant.PAYMENT_AMOUNT, TestConstant.ORDER_ID4, TestConstant.PAYMENT_KEY4, ProductType.RESERVATION, 4L)))
                .thenReturn(new PaymentApproveResponse(TestConstant.PAYMENT_KEY4, TestConstant.ORDER_ID4, TestConstant.PAYMENT_AMOUNT));
        when(paymentRestClientManager.apply(new PaymentApplyRequest(TestConstant.PAYMENT_AMOUNT, TestConstant.ORDER_ID5, TestConstant.PAYMENT_KEY5, ProductType.RESERVATION, 5L)))
                .thenReturn(new PaymentApproveResponse(TestConstant.PAYMENT_KEY5, TestConstant.ORDER_ID5, TestConstant.PAYMENT_AMOUNT));
        when(paymentRestClientManager.apply(new PaymentApplyRequest(TestConstant.PAYMENT_AMOUNT, TestConstant.ORDER_ID6, TestConstant.PAYMENT_KEY6, ProductType.RESERVATION, 6L)))
                .thenReturn(new PaymentApproveResponse(TestConstant.PAYMENT_KEY6, TestConstant.ORDER_ID6, TestConstant.PAYMENT_AMOUNT));
        when(paymentRestClientManager.apply(new PaymentApplyRequest(TestConstant.PAYMENT_AMOUNT, TestConstant.ORDER_ID7, TestConstant.PAYMENT_KEY7, ProductType.RESERVATION, 7L)))
                .thenReturn(new PaymentApproveResponse(TestConstant.PAYMENT_KEY7, TestConstant.ORDER_ID7, TestConstant.PAYMENT_AMOUNT));
        when(paymentRestClientManager.apply(new PaymentApplyRequest(TestConstant.PAYMENT_AMOUNT, TestConstant.ORDER_ID8, TestConstant.PAYMENT_KEY8, ProductType.RESERVATION, 8L)))
                .thenReturn(new PaymentApproveResponse(TestConstant.PAYMENT_KEY8, TestConstant.ORDER_ID8, TestConstant.PAYMENT_AMOUNT));
    }

    @Test
    void 예약을_저장할_수_있다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE);

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
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.WAITING, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE);

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
        reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE)))
                .isInstanceOf(DuplicatedReservationException.class)
                .hasMessage("이미 예약되어 있는 시각에는 예약할 수 없습니다.");
    }

    @Test
    void 존재하지_않는_멤버의_이메일으로는_예약을_등록할_수_없다() {
        // Given
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest("invalid@email.com", TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE);

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
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, TestConstant.INVALID_ENTITY_ID, theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE);

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
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), TestConstant.INVALID_ENTITY_ID, ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE);

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
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));

        // When & Then
        assertThat(reservationService.findReservations(null)).containsExactlyInAnyOrder(reservation);
    }

    @Test
    void 특정_상태를_가진_예약만_조회할_수_있다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        Reservation waitingReservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.WAITING, TestConstant.PAYMENT_KEY2, TestConstant.ORDER_ID2, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));

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
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        Reservation waitingReservation1 = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.WAITING, TestConstant.PAYMENT_KEY2, TestConstant.ORDER_ID2, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        Reservation waitingReservation2 = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.WAITING, TestConstant.PAYMENT_KEY3, TestConstant.ORDER_ID3, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        Reservation waitingReservation3 = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.WAITING, TestConstant.PAYMENT_KEY4, TestConstant.ORDER_ID4, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        List<ReservationMineGetWebResponse> expected = List.of(
                new ReservationMineGetWebResponse(reservation.getId(), reservation.getDate(), reservation.getTime().getStartAt(), reservation.getTheme().getName(), "예약", TestConstant.PAYMENT_KEY, 1000L),
                new ReservationMineGetWebResponse(waitingReservation1.getId(), waitingReservation1.getDate(), waitingReservation1.getTime().getStartAt(), waitingReservation1.getTheme().getName(), "1번째 대기", "-", 0L),
                new ReservationMineGetWebResponse(waitingReservation2.getId(), waitingReservation2.getDate(), waitingReservation2.getTime().getStartAt(), waitingReservation2.getTheme().getName(), "2번째 대기", "-", 0L),
                new ReservationMineGetWebResponse(waitingReservation3.getId(), waitingReservation3.getDate(), waitingReservation3.getTime().getStartAt(), waitingReservation3.getTheme().getName(), "3번째 대기", "-", 0L)
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
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        reservationService.createReservation(new ReservationCreateRequest(member2.getEmail(), TestConstant.FUTURE_DATE.plusDays(1), time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY2, TestConstant.ORDER_ID2, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));

        // When & Then
        assertThat(reservationService.findFilteredReservations(member.getId(), null, null, null)).containsExactlyInAnyOrder(reservation);
    }

    @Test
    void 예약을_테마로_필터링해서_조회할_수_있다() {
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Theme theme2 = themeRepository.save(new Theme("다른 테마 이름", "다른 테마 설명", "다른 테마 썸네일 URL"));
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme2.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY2, TestConstant.ORDER_ID2, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));

        // When & Then
        assertThat(reservationService.findFilteredReservations(null, theme.getId(), null, null)).containsExactlyInAnyOrder(reservation);
    }

    @Test
    void 예약을_날짜로_필터링해서_조회할_수_있다() {
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Reservation reservation1 = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        Reservation reservation2 = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE.plusDays(1), time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY2, TestConstant.ORDER_ID2, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        Reservation reservation3 = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE.plusDays(2), time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY3, TestConstant.ORDER_ID3, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE.plusDays(3), time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY4, TestConstant.ORDER_ID4, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE.plusDays(4), time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY5, TestConstant.ORDER_ID5, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));

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
        reservationService.createReservation(new ReservationCreateRequest(member1.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme1.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        reservationService.createReservation(new ReservationCreateRequest(member2.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme2.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY2, TestConstant.ORDER_ID2, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        reservationService.createReservation(new ReservationCreateRequest(member1.getEmail(), TestConstant.FUTURE_DATE.plusDays(1), time.getId(), theme2.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY3, TestConstant.ORDER_ID3, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        reservationService.createReservation(new ReservationCreateRequest(member2.getEmail(), TestConstant.FUTURE_DATE.plusDays(1), time.getId(), theme1.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY4, TestConstant.ORDER_ID4, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        reservationService.createReservation(new ReservationCreateRequest(member1.getEmail(), TestConstant.FUTURE_DATE.plusDays(2), time.getId(), theme1.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY5, TestConstant.ORDER_ID5, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        reservationService.createReservation(new ReservationCreateRequest(member2.getEmail(), TestConstant.FUTURE_DATE.plusDays(2), time.getId(), theme2.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY6, TestConstant.ORDER_ID6, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        reservationService.createReservation(new ReservationCreateRequest(member1.getEmail(), TestConstant.FUTURE_DATE.plusDays(3), time.getId(), theme2.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY7, TestConstant.ORDER_ID7, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        reservationService.createReservation(new ReservationCreateRequest(member2.getEmail(), TestConstant.FUTURE_DATE.plusDays(3), time.getId(), theme1.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY8, TestConstant.ORDER_ID8, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));

        // When & Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservationService.findFilteredReservations(member1.getId(), theme2.getId(), null, null)).hasSize(2);
            softAssertions.assertThat(reservationService.findFilteredReservations(member2.getId(), null, TestConstant.FUTURE_DATE.plusDays(1), TestConstant.FUTURE_DATE.plusDays(3))).hasSize(3);
            softAssertions.assertThat(reservationService.findFilteredReservations(member2.getId(), theme1.getId(), TestConstant.FUTURE_DATE.plusDays(1), TestConstant.FUTURE_DATE.plusDays(3))).hasSize(2);
        });
    }

    @Test
    void 대기_상태의_예약을_예약_상태로_바꿀_수_있다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Reservation waitingReservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.WAITING, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));

        // When
        Reservation reservedReservation = reservationService.applyWaitingReservation(waitingReservation.getId());

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservedReservation.getId()).isNotNull();
            softAssertions.assertThat(reservedReservation.getDate()).isEqualTo(waitingReservation.getDate());
            softAssertions.assertThat(reservedReservation.getTime()).isEqualTo(waitingReservation.getTime());
            softAssertions.assertThat(reservedReservation.getTheme()).isEqualTo(waitingReservation.getTheme());
            softAssertions.assertThat(reservedReservation.getMember()).isEqualTo(waitingReservation.getMember());
            softAssertions.assertThat(reservedReservation.getStatus()).isEqualTo(ReservationStatus.RESERVED);
        });
    }

    @Test
    void 대기_상태가_아닌_예약은_상태를_바꿀_수_없다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Reservation reservedReservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));

        // When & Then
        assertThatThrownBy(() -> reservationService.applyWaitingReservation(reservedReservation.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("대기 상태의 예약이 아닙니다.");
    }

    @Test
    void 이미_예약되어_있는_시각의_대기_상태의_예약은_예약_상태로_바꿀_수_없다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
        Reservation waitingReservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.WAITING, TestConstant.PAYMENT_KEY2, TestConstant.ORDER_ID2, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));

        // When & Then
        assertThatThrownBy(() -> reservationService.applyWaitingReservation(waitingReservation.getId()))
                .isInstanceOf(DuplicatedReservationException.class)
                .hasMessage("이미 예약되어 있는 시각에는 예약할 수 없습니다.");
    }

    @Test
    void 예약을_취소할_수_있다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        Theme theme = themeRepository.save(new Theme(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        Reservation createdReservation = reservationService.createReservation(new ReservationCreateRequest(member.getEmail(), TestConstant.FUTURE_DATE, time.getId(), theme.getId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE));
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
