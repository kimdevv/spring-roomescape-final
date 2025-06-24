package roomescape.reservation.business.dto.response;

import roomescape.reservation.model.ReservationTime;
import roomescape.reservation.model.Theme;

import java.time.LocalDate;

public record WaitingReservationWithRankGetResponse(Long id, LocalDate date, ReservationTime time, Theme theme, Long rank) {
}
