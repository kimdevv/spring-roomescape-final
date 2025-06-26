package roomescape.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.annotation.NormalLogin;
import roomescape.auth.business.AuthService;
import roomescape.auth.business.CookieManager;
import roomescape.auth.model.LoginInfo;
import roomescape.auth.presentation.dto.request.LoginRequest;
import roomescape.auth.presentation.dto.response.LoginCheckResponse;
import roomescape.common.exception.handler.ErrorResponse;

@RestController
@Tag(name = "인증", description = "인증과 관련된 API")
public class AuthController {

    public static final String TOKEN_COOKIE_NAME = "token";
    private final AuthService authService;
    private final CookieManager cookieManager;

    public AuthController(AuthService authService, CookieManager cookieManager) {
        this.authService = authService;
        this.cookieManager = cookieManager;
    }

    @Operation(summary = "로그인", description = "로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공 시 자동으로 헤더에 토큰이 등록됩니다.")
    })
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest requestBody) {
        String token = authService.login(requestBody);
        ResponseCookie tokenCookie = cookieManager.generateCookie(TOKEN_COOKIE_NAME, token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .build();
    }

    @Operation(summary = "로그인 확인", description = "로그인 상태인지 검사합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인이 되어 있는 경우라면 현재 로그인되어 있는 멤버의 이름을 응답합니다."),
            @ApiResponse(responseCode = "400", description = "로그인되지 않은 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @NormalLogin
    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(@Parameter(hidden = true) LoginInfo loginInfo, HttpServletRequest request) {
        String name = authService.findMemberNameByEmail(loginInfo.email());
        return ResponseEntity.status(HttpStatus.OK).body(new LoginCheckResponse(name));
    }
}
