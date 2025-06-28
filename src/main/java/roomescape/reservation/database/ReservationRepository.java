package roomescape.reservation.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import roomescape.reservation.business.dto.response.WaitingReservationWithRankGetResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    List<Reservation> findAll();

    @Query("""
        SELECT r
        FROM Reservation  r
        WHERE (:status IS NULL OR r.status = :status)
    """)
    List<Reservation> find(ReservationStatus status);

    List<Reservation> findByMemberIdAndStatus(Long memberId, ReservationStatus status);

    @Query("""
        SELECT new roomescape.reservation.business.dto.response.WaitingReservationWithRankGetResponse(r.id, r.date, r.time, r.theme, (
            SELECT COUNT(r2) + 1L
            FROM Reservation r2
            WHERE r2.date = r.date
                AND r2.time = r.time
                AND r2.theme = r.theme
                AND r2.id < r.id
                AND r2.status = 'WAITING'
        ))
        FROM Reservation r
        WHERE r.status = 'WAITING'
            AND r.member.id = :memberId
    """)
    List<WaitingReservationWithRankGetResponse> findWaitingReservationsWithRankByMemberId(Long memberId);

    @Query(value = """
        SELECT r
        FROM Reservation r
        WHERE (:memberId IS NULL OR r.member.id = :memberId)
            AND (:themeId IS NULL OR r.theme.id = :themeId)
            AND ((:startDate IS NULL OR :endDate IS NULL) OR r.date BETWEEN :startDate AND :endDate)
    """)
    List<Reservation> findFiltered(Long memberId, Long themeId, LocalDate startDate, LocalDate endDate);

    boolean existsByDateAndTimeIdAndThemeIdAndStatus(LocalDate date, Long timeId, Long themeId, ReservationStatus status);
}
