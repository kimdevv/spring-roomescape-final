package roomescape.reservation.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final LocalTime time;

    public Reservation(Long id, String name, LocalDate date, LocalTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation toEntity(Long id) {
        validateIdNull(id);
        return new Reservation(id, name, date, time);
    }

    private void validateIdNull(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id값은 null이 될 수 없습니다.");
        }
    }

    public boolean isIdNull() {
        return id == null;
    }
}
