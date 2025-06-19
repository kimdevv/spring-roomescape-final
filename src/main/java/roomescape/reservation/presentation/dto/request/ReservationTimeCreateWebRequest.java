package roomescape.reservation.presentation.dto.request;

import java.time.LocalTime;

public record ReservationTimeCreateWebRequest(LocalTime startAt) {
}
