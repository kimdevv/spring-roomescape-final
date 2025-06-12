package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class BaseCustomException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BaseCustomException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
