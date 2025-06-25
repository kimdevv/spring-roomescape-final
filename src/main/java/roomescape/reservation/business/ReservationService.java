package roomescape.reservation.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.database.MemberDoesNotExistException;
import roomescape.member.database.MemberRepository;
import roomescape.member.model.Member;
import roomescape.payment.business.PaymentService;
import roomescape.payment.business.dto.request.PaymentApplyRequest;
import roomescape.reservation.business.dto.request.ReservationCreateRequest;
import roomescape.reservation.business.dto.response.WaitingReservationWithRankGetResponse;
import roomescape.reservation.database.ReservationRepository;
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
import roomescape.reservation.presentation.dto.request.ReservationCreateByAdminWebRequest;
import roomescape.reservation.presentation.dto.response.ReservationMineGetWebResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final PaymentService paymentService;

    public ReservationService(MemberRepository memberRepository, ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository, PaymentService paymentService) {
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.paymentService = paymentService;
    }

    public Reservation createReservation(ReservationCreateByAdminWebRequest reservationCreateByAdminWebRequest) {
        LocalDate date = reservationCreateByAdminWebRequest.date();
        Long timeId = reservationCreateByAdminWebRequest.timeId();
        Long themeId = reservationCreateByAdminWebRequest.themeId();
        validateDuplicatedDateAndTimeAndTheme(date, timeId, themeId, ReservationStatus.RESERVED);
        Member member = memberRepository.findById(reservationCreateByAdminWebRequest.memberId())
                .orElseThrow(() -> new MemberDoesNotExistException("존재하지 않는 멤버의 id입니다."));
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeDoesNotExistException("존재하지 않는 예약시간 id입니다."));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeDoesNotExistException("존재하지 않는 테마 id입니다."));
        return reservationRepository.save(new Reservation(member, date, time, theme, ReservationStatus.RESERVED));
    }

    @Transactional
    public Reservation createReservation(ReservationCreateRequest reservationCreateRequest) {
        paymentService.applyPayment(new PaymentApplyRequest(reservationCreateRequest.amount(), reservationCreateRequest.orderId(), reservationCreateRequest.paymentKey()));
        LocalDate date = reservationCreateRequest.date();
        Long timeId = reservationCreateRequest.timeId();
        Long themeId = reservationCreateRequest.themeId();
        ReservationStatus status = reservationCreateRequest.status();
        validateDuplicatedDateAndTimeAndTheme(date, timeId, themeId, status);
        Member member = memberRepository.findByEmail(reservationCreateRequest.email())
                .orElseThrow(() -> new MemberDoesNotExistException("잘못된 멤버의 요청입니다."));
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeDoesNotExistException("존재하지 않는 예약시간 id입니다."));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeDoesNotExistException("존재하지 않는 테마 id입니다."));
        return reservationRepository.save(new Reservation(member, date, time, theme, status));
    }

    private void validateDuplicatedDateAndTimeAndTheme(LocalDate date, Long timeId, Long themeId, ReservationStatus status) {
        if (status == ReservationStatus.RESERVED && reservationRepository.existsByDateAndTimeIdAndThemeIdAndStatus(date, timeId, themeId, ReservationStatus.RESERVED)) {
            throw new DuplicatedReservationException("이미 예약되어 있는 시각에는 예약할 수 없습니다.");
        }
    }

    public List<Reservation> findReservations(ReservationStatus status) {
        return reservationRepository.find(status);
    }

    public List<ReservationMineGetWebResponse> findMyReservations(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberDoesNotExistException("존재하지 않는 멤버의 이메일입니다."));
        List<Reservation> reservations = reservationRepository.findByMemberIdAndStatus(member.getId(), ReservationStatus.RESERVED);
        List<WaitingReservationWithRankGetResponse> waitingReservations = reservationRepository.findWaitingReservationsWithRankByMemberId(member.getId());
        List<ReservationMineGetWebResponse> responses = reservations.stream()
                .map(reservation -> new ReservationMineGetWebResponse(reservation.getId(), reservation.getDate(), reservation.getTime().getStartAt(), reservation.getTheme().getName(), "예약"))
                .collect(Collectors.toList());
        for (WaitingReservationWithRankGetResponse waitingReservation : waitingReservations) {
            responses.add(new ReservationMineGetWebResponse(waitingReservation.id(), waitingReservation.date(), waitingReservation.time().getStartAt(), waitingReservation.theme().getName(), String.format("%d번째 대기", waitingReservation.rank())));
        }
        return responses;
    }

    public List<Reservation> findFilteredReservations(Long memberId, Long themeId, LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findFiltered(memberId, themeId, startDate, endDate);
    }

    @Transactional
    public Reservation applyWaitingReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationDoesNotExistException("존재하지 않는 예약 id입니다."));
        validateStatusIsWaiting(reservation.getStatus());
        LocalDate date = reservation.getDate();
        ReservationTime time = reservation.getTime();
        Theme theme = reservation.getTheme();
        validateDuplicatedDateAndTimeAndTheme(date, time.getId(), theme.getId(), ReservationStatus.RESERVED);
        reservationRepository.delete(reservation);
        return reservationRepository.save(new Reservation(reservation.getMember(), date, time, theme, ReservationStatus.RESERVED));
    }

    private void validateStatusIsWaiting(ReservationStatus status) {
        if (status == ReservationStatus.WAITING) {
            return;
        }
        throw new IllegalStateException("대기 상태의 예약이 아닙니다.");
    }

    @Transactional
    public void cancelReservation(Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return;
        }
        throw new ReservationDoesNotExistException("존재하지 않는 예약 id입니다.");
    }
}
