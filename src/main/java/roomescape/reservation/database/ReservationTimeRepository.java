package roomescape.reservation.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import roomescape.reservation.model.ReservationTime;
import roomescape.reservation.presentation.dto.response.ReservationTimeGetWithAvailabilityWebResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationTimeRepository extends CrudRepository<ReservationTime, Long> {

    List<ReservationTime> findAll();

    @Query(value = """
        SELECT new roomescape.reservation.presentation.dto.response.ReservationTimeGetWithAvailabilityWebResponse(rt.id, rt.startAt, r.id IS NULL)
        FROM ReservationTime rt
            LEFT OUTER JOIN Reservation r ON rt.id = r.time.id
                AND r.theme.id = :themeId
                AND r.date = :date
    """)
    List<ReservationTimeGetWithAvailabilityWebResponse> findAllWithAvailability(Long themeId, LocalDate date);

    boolean existsByStartAt(LocalTime startAt);
}
