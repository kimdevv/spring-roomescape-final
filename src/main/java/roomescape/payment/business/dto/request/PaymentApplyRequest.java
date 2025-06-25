package roomescape.payment.business.dto.request;

public record PaymentApplyRequest(Long amount, String orderId, String paymentKey) {

    public PaymentApplyRequest {
        if (amount == null) {
            throw new IllegalArgumentException("주문 금액은 null이 될 수 없습니다.");
        }
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("주문 번호는 빈 값이 될 수 없습니다.");
        }
        if (paymentKey == null || paymentKey.isBlank()) {
            throw new IllegalArgumentException("결제 키는 빈 값이 될 수 없습니다.");
        }
    }
}
