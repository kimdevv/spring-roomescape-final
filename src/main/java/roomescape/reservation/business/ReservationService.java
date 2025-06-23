package roomescape.reservation.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.database.MemberDoesNotExistException;
import roomescape.member.database.MemberRepository;
import roomescape.member.model.Member;
import roomescape.reservation.business.dto.request.ReservationCreateRequest;
import roomescape.reservation.database.ReservationRepository;
import roomescape.reservation.database.ReservationTimeRepository;
import roomescape.reservation.database.ThemeRepository;
import roomescape.reservation.exception.DuplicatedReservationException;
import roomescape.reservation.exception.ReservationDoesNotExistException;
import roomescape.reservation.exception.ReservationTimeDoesNotExistException;
import roomescape.reservation.exception.ThemeDoesNotExistException;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationTime;
import roomescape.reservation.model.Theme;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(MemberRepository memberRepository, ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Reservation createReservation(ReservationCreateRequest reservationCreateRequest) {
        LocalDate date = reservationCreateRequest.date();
        Long timeId = reservationCreateRequest.timeId();
        Long themeId = reservationCreateRequest.themeId();
        validateDuplicatedDateAndTimeAndTheme(date, timeId, themeId);
        Member member = memberRepository.findByEmail(reservationCreateRequest.email())
                .orElseThrow(() -> new MemberDoesNotExistException("잘못된 멤버의 요청입니다."));
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeDoesNotExistException("존재하지 않는 예약시간 id입니다."));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeDoesNotExistException("존재하지 않는 테마 id입니다."));
        return reservationRepository.save(new Reservation(member, date, time, theme));
    }

    private void validateDuplicatedDateAndTimeAndTheme(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new DuplicatedReservationException("이미 예약되어 있는 시각에는 예약할 수 없습니다.");
        }
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
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
