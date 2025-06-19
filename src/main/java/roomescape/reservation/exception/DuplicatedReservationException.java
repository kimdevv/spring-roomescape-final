package roomescape.reservation.exception;

import roomescape.common.exception.DuplicatedEntityException;

public class DuplicatedReservationException extends DuplicatedEntityException {

    public DuplicatedReservationException(String message) {
        super(message);
    }
}
