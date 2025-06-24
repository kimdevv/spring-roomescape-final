package roomescape.reservation.presentation.dto.request;

import roomescape.reservation.model.ReservationStatus;

import java.time.LocalDate;

public record ReservationCreateWebRequest(LocalDate date, Long timeId, Long themeId, ReservationStatus status) {

    public ReservationCreateWebRequest {
        validateDateTime(date, timeId);
        validateTheme(themeId);
        validateStatus(status);
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

    private void validateStatus(ReservationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("예약상태는 null이 될 수 없습니다.");
        }
    }
}
