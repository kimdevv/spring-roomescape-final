package roomescape.reservation.exception;

import roomescape.common.exception.EntityDoesNotExistsException;

public class ReservationDoesNotExistException extends EntityDoesNotExistsException {

    public ReservationDoesNotExistException(String message) {
        super(message);
    }
}
