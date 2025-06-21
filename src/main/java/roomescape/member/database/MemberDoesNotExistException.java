package roomescape.member.database;

import roomescape.common.exception.EntityDoesNotExistsException;

public class MemberDoesNotExistException extends EntityDoesNotExistsException {

    public MemberDoesNotExistException(String message) {
        super(message);
    }
}
