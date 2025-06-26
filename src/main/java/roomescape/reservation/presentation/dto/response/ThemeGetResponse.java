package roomescape.reservation.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ThemeGetResponse(
        @Schema(description = "테마의 기본 키") Long id,
        @Schema(description = "테마의 이름") String name,
        @Schema(description = "테마의 설명") String description,
        @Schema(description = "테마의 썸네일 이미지 Url") String thumbnail) {
}
