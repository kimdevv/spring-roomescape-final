package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.exception.BaseCustomException;

public class ForbiddenException extends BaseCustomException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
