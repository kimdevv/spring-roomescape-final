package roomescape.reservation.business;

import org.springframework.stereotype.Service;
import roomescape.reservation.business.dto.request.ThemeCreateRequest;
import roomescape.reservation.database.ThemeRepository;
import roomescape.reservation.exception.DuplicatedThemeException;
import roomescape.reservation.model.Theme;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme createTheme(ThemeCreateRequest themeCreateRequest) {
        String name = themeCreateRequest.name();
        validateDuplicatedName(name);
        return themeRepository.save(new Theme(name, themeCreateRequest.description(), themeCreateRequest.thumbnail()));
    }

    private void validateDuplicatedName(String name) {
        if (themeRepository.existsByName(name)) {
            throw new DuplicatedThemeException("이미 존재하는 테마의 이름입니다.");
        }
    }

    public List<Theme> findAllThemes() {
        return themeRepository.findAll();
    }
}
