package roomescape.payment.business;

import org.springframework.stereotype.Service;
import roomescape.payment.business.dto.request.PaymentApplyRequest;
import roomescape.payment.business.dto.response.PaymentApproveResponse;
import roomescape.payment.database.PaymentRepository;
import roomescape.payment.model.Payment;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentRestClientManager paymentRestClientManager;

    public PaymentService(PaymentRepository paymentRepository, PaymentRestClientManager paymentRestClientManager) {
        this.paymentRepository = paymentRepository;
        this.paymentRestClientManager = paymentRestClientManager;
    }

    public Payment applyPayment(PaymentApplyRequest paymentApplyRequest) {
        PaymentApproveResponse paymentApproveResponse = paymentRestClientManager.apply(paymentApplyRequest);
        return paymentRepository.save(new Payment(paymentApproveResponse.paymentKey(), paymentApproveResponse.orderId(), paymentApproveResponse.totalAmount(), paymentApplyRequest.productType(), paymentApplyRequest.productId()));
    }
}
