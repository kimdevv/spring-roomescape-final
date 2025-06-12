package roomescape.reservation.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.business.ReservationService;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.presentation.dto.request.ReservationCreateRequest;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationCreateRequest requestBody) {
        Reservation reservation = reservationService.createReservation(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping
    public List<Reservation> findAllReservations() {
        return reservationService.findAllReservations();
    }
}
