package roomescape.auth.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginCheckResponse(@Schema(description = "멤버의 이름") String name) {
}
