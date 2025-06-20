package roomescape.reservation.exception;

import roomescape.common.exception.DuplicatedEntityException;

public class DuplicatedThemeException extends DuplicatedEntityException {

    public DuplicatedThemeException(String message) {
        super(message);
    }
}
