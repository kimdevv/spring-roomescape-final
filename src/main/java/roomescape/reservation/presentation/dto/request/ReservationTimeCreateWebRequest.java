package roomescape.reservation.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record ReservationTimeCreateWebRequest(@Schema(description = "예약시간의 시작 시간") LocalTime startAt) {

    public ReservationTimeCreateWebRequest {
        if (startAt == null) {
            throw new IllegalArgumentException("시작시간은 null이 될 수 없습니다.");
        }
    }
}
