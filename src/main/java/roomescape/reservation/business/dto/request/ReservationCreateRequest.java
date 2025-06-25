package roomescape.reservation.business.dto.request;

import roomescape.reservation.model.ReservationStatus;

import java.time.LocalDate;

public record ReservationCreateRequest(String email, LocalDate date, Long timeId, Long themeId, ReservationStatus status, String paymentKey, String orderId, Long amount, String paymentType) {

    public ReservationCreateRequest {
        validateEmail(email);
        validateDateTime(date, timeId);
        validateTheme(themeId);
        validateStatus(status);
        validatePaymentInformation(paymentKey, orderId, amount, paymentType);
    }

    private void validateEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("이메일은 null이 될 수 없습니다.");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("이메일은 빈 값이 될 수 없습니다.");
        }
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
