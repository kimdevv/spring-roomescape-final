package roomescape.reservation.database;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.reservation.model.Theme;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    boolean existsByName(String name);
}
