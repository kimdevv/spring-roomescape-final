package roomescape.payment.business;

import org.springframework.stereotype.Service;
import roomescape.payment.business.dto.request.PaymentApplyRequest;

@Service
public class PaymentService {

    private final PaymentRestClientManager paymentRestClientManager;

    public PaymentService(PaymentRestClientManager paymentRestClientManager) {
        this.paymentRestClientManager = paymentRestClientManager;
    }

    public void applyPayment(PaymentApplyRequest paymentApplyRequest) {
        paymentRestClientManager.apply(paymentApplyRequest);
    }
}
