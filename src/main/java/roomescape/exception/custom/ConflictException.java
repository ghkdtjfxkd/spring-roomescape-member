package roomescape.exception.custom;

import roomescape.exception.ErrorMessage;

public class ConflictException extends RuntimeException {

    public ConflictException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
