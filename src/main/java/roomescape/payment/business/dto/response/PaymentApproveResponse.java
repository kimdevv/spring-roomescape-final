package roomescape.payment.business.dto.response;

public record PaymentApproveResponse(String paymentKey, String orderId, Long totalAmount) {
}
