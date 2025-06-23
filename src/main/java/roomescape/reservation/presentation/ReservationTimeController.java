package roomescape.reservation.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.annotation.AdminLogin;
import roomescape.auth.annotation.NormalLogin;
import roomescape.reservation.business.ReservationTimeService;
import roomescape.reservation.business.dto.request.ReservationTimeCreateRequest;
import roomescape.reservation.business.dto.request.ReservationTimeGetWithAvailabilityRequest;
import roomescape.reservation.model.ReservationTime;
import roomescape.reservation.presentation.dto.request.ReservationTimeCreateWebRequest;
import roomescape.reservation.presentation.dto.response.ReservationTimeGetWithAvailabilityWebResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @AdminLogin
    @PostMapping
    public ResponseEntity<ReservationTime> createReservationTime(@RequestBody ReservationTimeCreateWebRequest requestBody) {
        ReservationTime reservationTime = reservationTimeService.createReservationTime(new ReservationTimeCreateRequest(requestBody.startAt()));
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationTime);
    }

    @AdminLogin
    @GetMapping
    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeService.findAllReservationTimes();
    }

    @NormalLogin
    @GetMapping("/withAvailability")
    public List<ReservationTimeGetWithAvailabilityWebResponse> findAllReservationTimesWithAvailability(@RequestParam Long themeId, @RequestParam LocalDate date) {
        return reservationTimeService.findAllReservationTimesWithAvailability(new ReservationTimeGetWithAvailabilityRequest(themeId, date));
    }

    @AdminLogin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.deleteReservationTimeById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
