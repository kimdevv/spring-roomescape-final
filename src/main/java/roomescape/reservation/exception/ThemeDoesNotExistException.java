package roomescape.reservation.exception;

import roomescape.common.exception.EntityDoesNotExistsException;

public class ThemeDoesNotExistException extends EntityDoesNotExistsException {

    public ThemeDoesNotExistException(String message) {
        super(message);
    }
}
