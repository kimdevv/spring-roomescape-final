package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.BaseCustomException;

public class UnauthorizedException extends BaseCustomException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
