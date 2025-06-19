package roomescape.reservation.presentation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationCreateWebRequest(String name, LocalDate date, LocalTime time) {

    public ReservationCreateWebRequest {
        validateName(name);
        validateDateTime(date, time);
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null이 될 수 없습니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름은 빈 값이 될 수 없습니다.");
        }
    }

    private void validateDateTime(LocalDate date, LocalTime time) {
        if (date == null || time == null) {
            throw new IllegalArgumentException("날짜와 시간은 null이 될 수 없습니다.");
        }
    }
}
