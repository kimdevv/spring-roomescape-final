package roomescape.reservation.business;

import org.springframework.stereotype.Service;
import roomescape.reservation.business.dto.request.ReservationTimeCreateRequest;
import roomescape.reservation.database.ReservationTimeRepository;
import roomescape.reservation.exception.DuplicatedReservationTimeException;
import roomescape.reservation.model.ReservationTime;

import java.time.LocalTime;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime createReservationTime(ReservationTimeCreateRequest reservationTimeCreateRequest) {
        LocalTime startAt = reservationTimeCreateRequest.startAt();
        validateDuplicatedStartAt(startAt);
        return reservationTimeRepository.save(new ReservationTime(startAt));
    }

    private void validateDuplicatedStartAt(LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new DuplicatedReservationTimeException("이미 해당 시간은 등록되어 있습니다.");
        }
    }
}
