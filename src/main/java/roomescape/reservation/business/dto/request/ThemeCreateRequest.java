package roomescape.reservation.business.dto.request;

public record ThemeCreateRequest(String name, String description, String thumbnail) {

    public ThemeCreateRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마의 이름은 빈 칸일 수 없습니다.");
        }
    }
}
