package roomescape.reservation.presentation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationMineGetWebResponse(Long id, LocalDate date, LocalTime time, String theme, String status) {
}
