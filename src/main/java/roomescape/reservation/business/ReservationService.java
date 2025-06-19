package roomescape.reservation.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.database.ReservationRepository;
import roomescape.reservation.exception.DuplicatedReservationException;
import roomescape.reservation.exception.ReservationDoesNotExistException;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.presentation.dto.request.ReservationCreateRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(ReservationCreateRequest reservationCreateRequest) {
        LocalDate date = reservationCreateRequest.date();
        LocalTime time = reservationCreateRequest.time();
        validateDuplicatedDateAndTime(date, time);
        return reservationRepository.save(new Reservation(reservationCreateRequest.name(), reservationCreateRequest.date(), reservationCreateRequest.time()));
    }

    private void validateDuplicatedDateAndTime(LocalDate date, LocalTime time) {
        if (reservationRepository.existsByDateAndTime(date, time)) {
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
