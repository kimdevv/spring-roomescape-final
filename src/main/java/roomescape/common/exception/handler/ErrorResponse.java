package roomescape.common.exception.handler;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, String message) {
}
