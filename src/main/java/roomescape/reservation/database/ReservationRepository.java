package roomescape.reservation.database;

import roomescape.reservation.model.Reservation;

import java.util.List;

public interface ReservationRepository {

    public Reservation save(Reservation reservation);

    public List<Reservation> findAll();
}
