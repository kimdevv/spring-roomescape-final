package roomescape.reservation.business.dto.request;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(LocalTime startAt) {
}
