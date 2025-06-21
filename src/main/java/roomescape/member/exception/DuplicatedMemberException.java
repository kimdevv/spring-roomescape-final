package roomescape.member.exception;

import roomescape.common.exception.DuplicatedEntityException;

public class DuplicatedMemberException extends DuplicatedEntityException {

    public DuplicatedMemberException(String message) {
        super(message);
    }
}
