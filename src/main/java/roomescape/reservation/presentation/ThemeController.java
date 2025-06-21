package roomescape.reservation.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.business.ThemeService;
import roomescape.reservation.business.dto.request.ThemeCreateRequest;
import roomescape.reservation.model.Theme;
import roomescape.reservation.presentation.dto.request.ThemeCreateWebRequest;

import java.util.List;

@RestController
@RequestMapping("/reservations/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody ThemeCreateWebRequest requestBody) {
        Theme theme = themeService.createTheme(new ThemeCreateRequest(requestBody.name(), requestBody.description(), requestBody.thumbnail()));
        return ResponseEntity.status(HttpStatus.CREATED).body(theme);
    }

    @GetMapping
    public List<Theme> findAllThemes() {
        return themeService.findAllThemes();
    }

    @GetMapping("/popular")
    public List<Theme> findPopularThemes() {
        return themeService.findPopularThemes();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteThemeById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
