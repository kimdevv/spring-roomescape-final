package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedEntityException extends BaseCustomException {

    public DuplicatedEntityException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
