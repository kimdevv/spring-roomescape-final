package roomescape.reservation.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.reservation.model.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByDateAndTimeId(LocalDate date, Long timeId);
}
