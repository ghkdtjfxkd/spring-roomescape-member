package roomescape.exception.custom;

import roomescape.exception.ErrorMessage;

public class BadRequestException extends RuntimeException {

    public BadRequestException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
