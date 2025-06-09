package roomescape.reservation.business;

import org.springframework.stereotype.Service;
import roomescape.reservation.database.ReservationRepository;
import roomescape.reservation.model.Reservation;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }
}
