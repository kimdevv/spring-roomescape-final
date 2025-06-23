package roomescape.reservation.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.reservation.model.Reservation;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query(value = """
        SELECT r 
        FROM Reservation r
        WHERE (:memberId IS NULL OR r.member.id = :memberId)
            AND (:themeId IS NULL OR r.theme.id = :themeId)
            AND ((:startDate IS NULL OR :endDate IS NULL) OR r.date BETWEEN :startDate AND :endDate)
    """)
    List<Reservation> findFiltered(Long memberId, Long themeId, LocalDate startDate, LocalDate endDate);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);
}
