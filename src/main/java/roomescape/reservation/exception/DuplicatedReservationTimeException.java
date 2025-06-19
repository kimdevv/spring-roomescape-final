package roomescape.reservation.exception;

import roomescape.common.exception.DuplicatedEntityException;

public class DuplicatedReservationTimeException extends DuplicatedEntityException {

    public DuplicatedReservationTimeException(String message) {
        super(message);
    }
}
