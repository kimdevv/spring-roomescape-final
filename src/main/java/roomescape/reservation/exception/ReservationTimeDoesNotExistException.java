package roomescape.reservation.exception;

import roomescape.common.exception.EntityDoesNotExistsException;

public class ReservationTimeDoesNotExistException extends EntityDoesNotExistsException {

    public ReservationTimeDoesNotExistException(String message) {
        super(message);
    }
}
