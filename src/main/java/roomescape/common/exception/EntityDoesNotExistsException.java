package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class EntityDoesNotExistsException extends BaseCustomException {

    public EntityDoesNotExistsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
