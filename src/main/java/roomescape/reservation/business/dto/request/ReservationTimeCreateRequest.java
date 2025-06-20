package roomescape.reservation.business.dto.request;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(LocalTime startAt) {

    public ReservationTimeCreateRequest {
        if (startAt == null) {
            throw new IllegalArgumentException("시작시간은 null이 될 수 없습니다.");
        }
    }
}
