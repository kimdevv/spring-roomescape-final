package roomescape.reservation.business;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import roomescape.reservation.database.ThemeRepository;
import roomescape.reservation.exception.DuplicatedThemeException;
import roomescape.reservation.exception.ThemeDoesNotExistException;
import roomescape.reservation.model.Theme;
import roomescape.reservation.presentation.dto.request.ThemeCreateWebRequest;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme createTheme(ThemeCreateWebRequest themeCreateWebRequest) {
        String name = themeCreateWebRequest.name();
        validateDuplicatedName(name);
        return themeRepository.save(new Theme(name, themeCreateWebRequest.description(), themeCreateWebRequest.thumbnail()));
    }

    private void validateDuplicatedName(String name) {
        if (themeRepository.existsByName(name)) {
            throw new DuplicatedThemeException("이미 존재하는 테마의 이름입니다.");
        }
    }

    public List<Theme> findAllThemes() {
        return themeRepository.findAll();
    }

    public void deleteThemeById(Long id) {
        if (themeRepository.existsById(id)) {
            themeRepository.deleteById(id);
            return;
        }
        throw new ThemeDoesNotExistException("존재하지 않는 테마 id입니다.");
    }

    public List<Theme> findPopularThemes() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(ReservationConstants.POPULAR_THEMES_PERIOD_DAYS);
        return themeRepository.findPopular(startDate, endDate, PageRequest.of(0, ReservationConstants.POPULAR_THEMES_COUNT)).getContent();
    }
}
