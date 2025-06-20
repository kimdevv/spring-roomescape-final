package roomescape.reservation.presentation.dto.request;

import java.time.LocalDate;

public record ReservationCreateWebRequest(String name, LocalDate date, Long timeId, Long themeId) {

    public ReservationCreateWebRequest {
        validateName(name);
        validateDateTime(date, timeId);
        validateTheme(themeId);
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null이 될 수 없습니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름은 빈 값이 될 수 없습니다.");
        }
    }

    private void validateDateTime(LocalDate date, Long timeId) {
        if (date == null || timeId == null) {
            throw new IllegalArgumentException("날짜와 시간은 null이 될 수 없습니다.");
        }
    }

    private void validateTheme(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("테마는 null이 될 수 없습니다.");
        }
    }
}
