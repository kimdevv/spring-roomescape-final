package roomescape.reservation.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record ReservationTimeGetWithAvailabilityWebResponse(
        @Schema(description = "예약시간의 기본 키") Long id,
        @Schema(description = "예약시간의 시작 시간") LocalTime startAt,
        @Schema(description = "현재 해당 예약시간으로의 예약 가능 여부") boolean isAvailable) {
}
