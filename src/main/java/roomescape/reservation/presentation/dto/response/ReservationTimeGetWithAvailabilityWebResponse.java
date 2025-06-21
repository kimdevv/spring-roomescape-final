package roomescape.reservation.presentation.dto.response;

import java.time.LocalTime;

public record ReservationTimeGetWithAvailabilityWebResponse(Long id, LocalTime startAt, boolean isAvailable) {
}
