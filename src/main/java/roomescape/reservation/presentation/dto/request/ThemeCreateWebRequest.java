package roomescape.reservation.presentation.dto.request;

public record ThemeCreateWebRequest(String name, String description, String thumbnail) {

    public ThemeCreateWebRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마의 이름은 빈 칸일 수 없습니다.");
        }
    }
}
