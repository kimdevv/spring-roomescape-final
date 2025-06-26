package roomescape.reservation.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.annotation.AdminLogin;
import roomescape.auth.annotation.NormalLogin;
import roomescape.common.config.SwaggerConfig;
import roomescape.common.exception.handler.ErrorResponse;
import roomescape.reservation.business.ThemeService;
import roomescape.reservation.model.Theme;
import roomescape.reservation.presentation.dto.request.ThemeCreateWebRequest;
import roomescape.reservation.presentation.dto.response.ThemeGetResponse;

import java.util.List;

@RestController
@RequestMapping("/reservations/themes")
@Tag(name = "테마", description = "테마와 관련된 API")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @Operation(summary = "테마 생성", description = "테마를 생성합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "테마 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터가 잘못된 경우", content = @Content(mediaType = "applicatoin/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @AdminLogin
    @PostMapping
    public ResponseEntity<ThemeGetResponse> createTheme(@RequestBody ThemeCreateWebRequest requestBody) {
        Theme theme = themeService.createTheme(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ThemeGetResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail()));
    }

    @Operation(summary = "모든 테마 조회", description = "저장되어 있는 모든 테마를 조회합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "테마 조회 성공")
    })
    @NormalLogin
    @GetMapping
    public List<ThemeGetResponse> findAllThemes() {
        List<Theme> themes = themeService.findAllThemes();
        return themes.stream()
                .map(theme -> new ThemeGetResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail()))
                .toList();
    }

    @Operation(summary = "인기 테마 조회", description = "일주일 전부터 오늘까지의 인기 테마를 상위 10개 순서대로 조회합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "테마 조회 성공")
    })
    @NormalLogin
    @GetMapping("/popular")
    public List<ThemeGetResponse> findPopularThemes() {
        List<Theme> themes = themeService.findPopularThemes();
        return themes.stream()
                .map(theme -> new ThemeGetResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail()))
                .toList();
    }

    @Operation(summary = "테마 삭제", description = "저장되어 있는 테마를 삭제합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "테마 삭제 성공")
    })
    @AdminLogin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable @Parameter(description = "삭제할 테마의 id") Long id) {
        themeService.deleteThemeById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
