package roomescape.reservation.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record ReservationTimeGetResponse(
        @Schema(description = "예약시간의 기본 키") Long id,
        @Schema(description = "예약시간의 시작 시간") LocalTime startAt) {
}
