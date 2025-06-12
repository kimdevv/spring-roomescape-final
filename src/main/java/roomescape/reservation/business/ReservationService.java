package roomescape.reservation.business;

import org.springframework.stereotype.Service;
import roomescape.reservation.database.ReservationRepository;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.presentation.dto.request.ReservationCreateRequest;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(ReservationCreateRequest reservationCreateRequest) {
        return reservationRepository.save(new Reservation(reservationCreateRequest.name(), reservationCreateRequest.date(), reservationCreateRequest.time()));
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }
}
