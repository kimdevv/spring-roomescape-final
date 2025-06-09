package roomescape.reservation.database;

import org.springframework.stereotype.Repository;
import roomescape.reservation.model.Reservation;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryReservationRepository implements ReservationRepository {

    private final List<Reservation> reservations;
    private final AtomicLong idGenerator = new AtomicLong(1);

    public MemoryReservationRepository(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public Reservation save(Reservation reservation) {
        if (reservation.isIdNull()) {
            Reservation savedReservation = reservation.toEntity(idGenerator.getAndIncrement());
            reservations.add(savedReservation);
            return savedReservation;
        }
        throw new IllegalArgumentException("이미 DB에 저장되어 있는 엔티티입니다.");
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }
}
