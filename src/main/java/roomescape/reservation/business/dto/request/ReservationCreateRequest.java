package roomescape.reservation.business.dto.request;

import roomescape.reservation.model.ReservationStatus;

import java.time.LocalDate;

public record ReservationCreateRequest(String email, LocalDate date, Long timeId, Long themeId, ReservationStatus status) {

    public ReservationCreateRequest {
        validateEmail(email);
        validateDateTime(date, timeId);
        validateTheme(themeId);
        validateStatus(status);
    }

    private void validateEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("이메일은 null이 될 수 없습니다.");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("이메일은 빈 값이 될 수 없습니다.");
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

    private void validateStatus(ReservationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("예약상태는 null이 될 수 없습니다.");
        }
    }
}
