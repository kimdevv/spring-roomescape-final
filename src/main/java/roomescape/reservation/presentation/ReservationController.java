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
import roomescape.auth.annotation.AdminLogin;
import roomescape.auth.annotation.NormalLogin;
import roomescape.auth.model.LoginInfo;
import roomescape.reservation.business.ReservationService;
import roomescape.reservation.business.dto.request.ReservationCreateRequest;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.presentation.dto.request.ReservationCreateByAdminWebRequest;
import roomescape.reservation.presentation.dto.request.ReservationCreateWebRequest;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @NormalLogin
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationCreateWebRequest requestBody, LoginInfo loginInfo) {
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(loginInfo.email(), requestBody.date(), requestBody.timeId(), requestBody.themeId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @AdminLogin
    @PostMapping("/admin")
    public ResponseEntity<Reservation> createReservationByAdmin(@RequestBody ReservationCreateByAdminWebRequest requestBody) {
        Reservation reservation = reservationService.createReservation(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @AdminLogin
    @GetMapping
    public List<Reservation> findAllReservations() {
        return reservationService.findAllReservations();
    }

    @NormalLogin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
