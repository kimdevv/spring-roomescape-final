package roomescape.reservation.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationMineGetWebResponse(
        @Schema(description = "예약의 기본 키") Long id,
        @Schema(description = "예약한 날짜") LocalDate date,
        @Schema(description = "예약한 시간") LocalTime time,
        @Schema(description = "예약한 테마의 이름") String theme,
        @Schema(description = "예약의 상태") String status,
        @Schema(description = "예약에 대한 결제 키") String paymentKey,
        @Schema(description = "예약에 대한 결제 금액") Long amount) {
}
