package roomescape.payment.business.dto.request;

import roomescape.payment.model.ProductType;

public record PaymentApplyRequest(Long amount, String orderId, String paymentKey, ProductType productType, Long productId) {

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
        if (productType == null) {
            throw new IllegalArgumentException("상품 종류는 null이 될 수 없습니다.");
        }
        if (productId == null) {
            throw new IllegalArgumentException("상품 id는 null이 될 수 없습니다.");
        }
    }
}
