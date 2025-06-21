package roomescape.reservation.business.dto.request;

import java.time.LocalDate;

public record ReservationTimeGetWithAvailabilityRequest(Long themeId, LocalDate date) {

    public ReservationTimeGetWithAvailabilityRequest {
        if (themeId == null) {
            throw new IllegalArgumentException("테마는 null이 될 수 없습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("날짜는 null이 될 수 없습니다.");
        }
    }
}
