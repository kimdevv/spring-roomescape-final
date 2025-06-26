package roomescape.reservation.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ThemeCreateWebRequest(
        @Schema(description = "테마의 이름") String name,
        @Schema(description = "테마의 설명") String description,
        @Schema(description = "테마의 썸네일 이미지 Url") String thumbnail) {

    public ThemeCreateWebRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마의 이름은 빈 칸일 수 없습니다.");
        }
    }
}
