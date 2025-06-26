package roomescape.reservation.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import roomescape.reservation.model.ReservationStatus;

import java.time.LocalDate;

public record ReservationCreateWebRequest(
        @Schema(description = "예약할 날짜") LocalDate date,
        @Schema(description = "예약할 시간의 id") Long timeId,
        @Schema(description = "예약할 테마의 id") Long themeId,
        @Schema(description = "예약할 상태") ReservationStatus status,
        @Schema(description = "예약에 대한 결제 키") String paymentKey,
        @Schema(description = "예약에 대한 주문 번호") String orderId,
        @Schema(description = "예약에 대한 결제 금액") Long amount,
        @Schema(description = "예약에 대한 주문 종류") String paymentType) {

    public ReservationCreateWebRequest {
        validateDateTime(date, timeId);
        validateTheme(themeId);
        validateStatus(status);
        validatePaymentInformation(paymentKey, orderId, amount, paymentType);
    }

    private void validateDateTime(LocalDate date, Long timeId) {
        if (date == null || timeId == null) {
            throw new IllegalArgumentException("날짜와 시간은 null이 될 수 없습니다.");
        }
    }

    private void validateTheme(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("테마는 null이 될 수 없습니다.");
        }
    }

    private void validateStatus(ReservationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("예약상태는 null이 될 수 없습니다.");
        }
    }

    private void validatePaymentInformation(String paymentKey, String orderId, Long amount, String paymentType) {
        if (paymentKey == null || paymentKey.isBlank()) {
            throw new IllegalArgumentException("결제 키는 빈 값이 될 수 없습니다.");
        }
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("주문 번호는 빈 값이 될 수 없습니다.");
        }
        if (amount == null) {
            throw new IllegalArgumentException("주문 금액은 null이 될 수 없습니다.");
        }
        if (paymentType == null || paymentType.isBlank()) {
            throw new IllegalArgumentException("주문 종류는 null이 될 수 없습니다.");
        }
    }
}
