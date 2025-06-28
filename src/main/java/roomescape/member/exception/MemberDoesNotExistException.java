package roomescape.member.exception;

import roomescape.common.exception.EntityDoesNotExistsException;

public class MemberDoesNotExistException extends EntityDoesNotExistsException {

    public MemberDoesNotExistException(String message) {
        super(message);
    }
}
