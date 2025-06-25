package roomescape.payment.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.BaseCustomException;

public class PaymentApplyException extends BaseCustomException {

    public PaymentApplyException(String message, HttpStatus status) {
        super(message, status);
    }
}
