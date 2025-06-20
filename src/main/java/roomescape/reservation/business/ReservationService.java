package roomescape.reservation.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.business.dto.request.ReservationCreateRequest;
import roomescape.reservation.database.ReservationRepository;
import roomescape.reservation.database.ReservationTimeRepository;
import roomescape.reservation.exception.DuplicatedReservationException;
import roomescape.reservation.exception.ReservationDoesNotExistException;
import roomescape.reservation.exception.ReservationTimeDoesNotExistException;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationTime;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    public Reservation createReservation(ReservationCreateRequest reservationCreateRequest) {
        LocalDate date = reservationCreateRequest.date();
        Long timeId = reservationCreateRequest.timeId();
        validateDuplicatedDateAndTime(date, timeId);
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeDoesNotExistException("존재하지 않는 예약시간 id입니다."));
        return reservationRepository.save(new Reservation(reservationCreateRequest.name(), date, time));
    }

    private void validateDuplicatedDateAndTime(LocalDate date, Long timeId) {
        if (reservationRepository.existsByDateAndTimeId(date, timeId)) {
            throw new DuplicatedReservationException("중복된 시각에는 예약할 수 없습니다.");
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
