package roomescape.reservation.presentation.dto.request;

import java.time.LocalTime;

public record ReservationTimeCreateWebRequest(LocalTime startAt) {

    public ReservationTimeCreateWebRequest {
        if (startAt == null) {
            throw new IllegalArgumentException("시작시간은 null이 될 수 없습니다.");
        }
    }
}
