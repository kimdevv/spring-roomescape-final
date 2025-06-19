package roomescape.reservation.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.business.ReservationTimeService;
import roomescape.reservation.business.dto.request.ReservationTimeCreateRequest;
import roomescape.reservation.model.ReservationTime;
import roomescape.reservation.presentation.dto.request.ReservationTimeCreateWebRequest;

@RestController
@RequestMapping("/reservations/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTime> createReservationTime(@RequestBody ReservationTimeCreateWebRequest requestBody) {
        ReservationTime reservationTime = reservationTimeService.createReservationTime(new ReservationTimeCreateRequest(requestBody.startAt()));
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.deleteReservationTimeById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
