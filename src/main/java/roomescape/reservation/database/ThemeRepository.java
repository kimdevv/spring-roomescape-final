package roomescape.reservation.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.reservation.model.Theme;

import java.time.LocalDate;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    @Query(value = """
        SELECT t
        FROM Theme t
            LEFT JOIN Reservation r ON t.id = r.theme.id
        WHERE r.date BETWEEN :startDate AND :endDate
        GROUP BY t.id
        ORDER BY COUNT(r.id) desc
    """)
    Page<Theme> findPopular(LocalDate startDate, LocalDate endDate, Pageable pageable);

    boolean existsByName(String name);
}
