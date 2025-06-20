package roomescape.reservation.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.business.ThemeService;
import roomescape.reservation.business.dto.request.ThemeCreateRequest;
import roomescape.reservation.model.Theme;
import roomescape.reservation.presentation.dto.request.ThemeCreateWebRequest;

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
}
