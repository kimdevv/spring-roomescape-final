package roomescape.reservation.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.business.ReservationService;
import roomescape.reservation.business.dto.request.ReservationCreateRequest;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.presentation.dto.request.ReservationCreateWebRequest;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationCreateWebRequest requestBody) {
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(requestBody.name(), requestBody.date(), requestBody.timeId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping
    public List<Reservation> findAllReservations() {
        return reservationService.findAllReservations();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
