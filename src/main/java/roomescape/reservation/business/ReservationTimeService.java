package roomescape.reservation.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.business.dto.request.ReservationTimeCreateRequest;
import roomescape.reservation.database.ReservationTimeRepository;
import roomescape.reservation.exception.DuplicatedReservationTimeException;
import roomescape.reservation.exception.ReservationTimeDoesNotExistException;
import roomescape.reservation.model.ReservationTime;

import java.time.LocalTime;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
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

    @Transactional
    public void deleteReservationTimeById(Long id) {
        if (reservationTimeRepository.existsById(id)) {
            reservationTimeRepository.deleteById(id);
        }
        throw new ReservationTimeDoesNotExistException("존재하지 않는 예약시간 id입니다.");
    }
}
